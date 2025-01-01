package org.pcub.extension;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.geysermc.cumulus.form.CustomForm;
import org.pcub.extension.feature.Stacker;

import java.util.ArrayList;
import java.util.UUID;

public class CommandExecuter implements CommandExecutor, TabExecutor {
    private final Common common;
    private final Main main;

    //用户命令
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            //控制台
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("debug")) {
                    if (!common.debug) {
                        common.debug = true;
                        main.logger.info("已开启：调试日志");
                    } else {
                        common.debug = false;
                        main.logger.info("已关闭：调试日志");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("legacyStack")) {
                    if (!common.legacyStack) {
                        common.legacyStack = true;
                        main.logger.info("已开启：强制堆叠 & 交易伪叠放功能");
                    } else {
                        common.legacyStack = false;
                        main.logger.warning("注意：如果 Geyser 未支持自定义物品最大堆叠数量，将会导致基岩版无法正常使用丹药。");
                        main.logger.info("已关闭：强制堆叠 & 交易伪叠放功能");
                    }
                    return true;
                }
                main.logger.warning("错误: 不正确的参数");
            }
            main.logger.info("支持的命令：/pcub <debug|legacyStack>");
            return true;
        }
        Player targetPlayer = (Player) sender;
        String targetName = targetPlayer.getName();
        UUID targetIDN = targetPlayer.getUniqueId();
        boolean isGeyser = common.geyserValid && common.geyserApi.isBedrockPlayer(targetIDN);
        boolean isFloodgate = common.floodgateValid && common.floodgateApi.isFloodgatePlayer(targetIDN);
        String locale = targetPlayer.getLocale();
        boolean isCN = locale.equalsIgnoreCase("zh_cn");
        // pcub stack
        if (args.length >= 1 && args[0].equalsIgnoreCase("stack")) new BukkitRunnable(){
            //强制叠放物品
            @Override
            public void run(){
                sender.sendMessage("§a" + ((isCN) ? "已强制叠放" : "已強制疊放") + " §b" + Stacker.forceStack(targetPlayer, 64) + ((isCN) ? " 种" : " 種") + "物品");
            }
        }.runTaskAsynchronously(main);
        // pcub dropContinuous
        else if (args.length >= 1 && args[0].equalsIgnoreCase("dropContinuous")) new BukkitRunnable(){
            //连续投掷
            @Override
            public void run(){
                String[] setResult = (isCN) ? new String[]{"始终禁用", "始终启用", "仅基岩版禁用", "仅 Android/iOS/WP 禁用"} : new String[]{"始終禁用", "始終啟用", "僅 Bedrock 版禁用", "僅 Android/iOS/WP 禁用"};
                if (args.length >= 2) {
                    int toInt;
                    if (args[1].equalsIgnoreCase("mobileOnly")) toInt = 3;
                    else if (args[1].equalsIgnoreCase("bedrockOnly")) toInt = 2;
                    else if (args[1].equalsIgnoreCase("enable")) toInt = 1;
                    else if (args[1].equalsIgnoreCase("disable")) toInt = 0;
                    else toInt = -1;
                    if (toInt >= 0) {
                        common.setScore("pcub_enable_continuous", targetName, toInt);
                        sender.sendMessage("§a" + ((isCN) ? "连续投掷 已设置为" : "連續投擲 已設定為") + ": §b" + setResult[toInt]);
                    } else sender.sendMessage((isCN) ? "§c错误: 不正确的参数。§r\n去掉所有参数以获得帮助。\n(如需获取当前值请去掉最后一个参数)" : "§c錯誤: 不正確的引數。§r\n去掉所有引數以獲得幫助。\n(如需獲取當前值請去掉最後一個引數)");
                } else sender.sendMessage("§a" + ((isCN) ? "当前 连续投掷 为" : "當前 連續投擲 為") + ": §b" + setResult[common.getScore("pcub_enable_continuous", targetName)]);
            }
        }.runTaskAsynchronously(main);
        // pcub dropInterval
        else if (args.length >= 1 && args[0].equalsIgnoreCase("dropInterval")) new BukkitRunnable(){
            //每次投掷间隔
            @Override
            public void run(){
                if (args.length >= 2) {
                    if (args[1].matches("[0-9]*")) {
                        int toInt = Integer.parseInt(args[1]);
                        if (toInt <= 20) {
                            common.setScore("pcub_drop_interval", targetName, toInt);
                            sender.sendMessage("§a" + ((isCN) ? "连续投掷间隔 已设置为" : "連續投擲間隔 已設定為") + ": §b" + args[1] + " 刻");
                        } else sender.sendMessage((isCN) ? "§c错误: 此值不能超过 20。" : "§c錯誤: 此值不能超過 20。");
                    } else sender.sendMessage((isCN) ? "§c错误: 需要整数。§r\n去掉所有参数以获得帮助。\n(如需获取当前值请去掉最后一个参数)" : "§c錯誤: 需要整數。§r\n去掉所有引數以獲得幫助。\n(如需獲取當前值請去掉最後一個引數)");
                } else sender.sendMessage("§a" + ((isCN) ? "当前 连续投掷间隔 为" : "當前 連續投擲間隔 為") + ": §b" + common.getScore("pcub_drop_interval", targetName) + " 刻");
            }
        }.runTaskAsynchronously(main);
        // pcub fastSkill
        else if (args.length >= 1 && args[0].equalsIgnoreCase("fastSkill")) new BukkitRunnable(){
            //按住潜行发动技能
            @Override
            public void run(){
                if (common.getScore("job", targetName) == 0) {
                    String[] setResult = (isCN) ? new String[]{"始终禁用", "始终启用", "仅基岩版启用", "仅 Android/iOS/WP 启用"} : new String[]{"始終禁用", "始終啟用", "僅 Bedrock 版啟用", "僅 Android/iOS/WP 啟用"};
                    if (args.length >= 2) {
                        int toInt;
                        if (args[1].equalsIgnoreCase("mobileOnly")) toInt = 3;
                        else if (args[1].equalsIgnoreCase("bedrockOnly")) toInt = 2;
                        else if (args[1].equalsIgnoreCase("enable")) toInt = 1;
                        else if (args[1].equalsIgnoreCase("disable")) toInt = 0;
                        else toInt = -1;
                        if (toInt >= 0) {
                            common.setScore("pcub_enable_fastskill", targetName, toInt);
                            sender.sendMessage("§a" + ((isCN) ? "按住潜行发动技能 已设置为" : "按住潛行發動技能 已設定為") + ": §b" + setResult[toInt]);
                        } else sender.sendMessage((isCN) ? "§c错误: 不正确的参数。§r\n去掉所有参数以获得帮助。\n(如需获取当前值请去掉最后一个参数)" : "§c錯誤: 不正確的引數。§r\n去掉所有引數以獲得幫助。\n(如需獲取當前值請去掉最後一個引數)");
                    } else sender.sendMessage("§a" + ((isCN) ? "当前 按住潜行发动技能 为" : "當前 按住潛行發動技能 為") + ": §b" + setResult[common.getScore("pcub_enable_fastskill", targetName)]);
                } else sender.sendMessage((isCN) ? "§c错误: 本功能仅限战士使用。" : "§c錯誤: 本功能僅限戰士使用。");
            }
        }.runTaskAsynchronously(main);
        // pcub fastSkillDuration
        else if (args.length >= 1 && args[0].equalsIgnoreCase("fastSkillDuration")) new BukkitRunnable(){
            //发动技能所需时长
            @Override
            public void run(){
                if (common.getScore("job", targetName) == 0) {
                    if (args.length >= 2) {
                        if (args[1].matches("[0-9]*")) {
                            int toInt = Integer.parseInt(args[1]);
                            if (toInt <= 20) {
                                common.setScore("pcub_fastskill_duration", targetName, toInt);
                                sender.sendMessage("§a" + ((isCN) ? "潜行技能所需时长 已设置为" : "潛行技能所需時長 已設定為") + ": §b" + args[1] + " 刻");
                            } else sender.sendMessage((isCN) ? "§c错误: 此值不能超过 20。" : "§c錯誤: 此值不能超過 20。");
                        } else sender.sendMessage((isCN) ? "§c错误: 需要整数。§r\n去掉所有参数以获得帮助。\n(如需获取当前值请去掉最后一个参数)" : "§c錯誤: 需要整數。§r\n去掉所有引數以獲得幫助。\n(如需獲取當前值請去掉最後一個引數)");
                    } else sender.sendMessage("§a" + ((isCN) ? "当前 潜行技能所需时长 为" : "當前 潛行技能所需時長 為") + ": §b" + common.getScore("pcub_fastskill_duration", targetName) + " 刻");
                } else sender.sendMessage((isCN) ? "§c错误: 本功能仅限战士使用。" : "§c錯誤: 本功能僅限戰士使用。");
            }
        }.runTaskAsynchronously(main);
        // pcub option
        else if (args.length >= 1 && args[0].equalsIgnoreCase("option")) {
            //基岩Forms菜单
            if (!common.geyserValid && !common.floodgateValid) {
                sender.sendMessage("§c" + ((isCN) ? "至少需要安装 Floodgate 和 Geyser-Spigot 插件中的任意一个，才能使用此功能。" : "至少需要安裝 Floodgate 和 Geyser-Spigot 插件中的任意一個，才能使用此功能。"));
                return true;
            } else if (!isFloodgate && !isGeyser) {
                sender.sendMessage("§c" + ((isCN) ? "该命令只能由基岩版的玩家执行！" : "這個指令只能由基岩版玩家執行！"));
                return true;
            } else if (args.length < 2) return true;
            //基岩版设置界面
            if (args[1].equalsIgnoreCase("combat")) new BukkitRunnable() {
                @Override
                public void run(){
                    int job = common.getScore("job", targetName);
                    CustomForm.Builder optForm = CustomForm.builder()
                            .title("pcub.title.combat_option")
                            .label("pcub.content.combat_option");
                    //武器激活位置
                    if (job != 2) {
                        strIs = 0;
                        int currentSlot = common.getScore("WeaponSlot", targetName) - 1;
                        if (currentSlot == -2) currentSlot = 9;
                        String name = "武器激活位置";
                        String[] s = {" 主手", "", "一", "二", "三", "四", "五", "六", "七", "八", "九", ""};
                        s[1] = (isCN) ? "号位" :"號位";
                        if (job == 1 || currentSlot == 9) {
                            s[11] = (isCN) ? ((job == 0) ? " 副手 （战士不可使用）" : " 副手 （基岩版暂不支持， 炼丹炉除外）") : ((job == 0) ? " 副手 （戰士不可使用）" : "副手 （Bedrock 版暫不支援， 煉丹爐除外）");
                            optForm.stepSlider(name, currentSlot, mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s));
                        } else
                            optForm.stepSlider(name, currentSlot, mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s), mst(s));
                    }
                    //连续投掷&每次投掷间隔
                    String dropTitle = (isCN) ? "退火符/击退符 连续投掷" : "退火符/擊退符 連續投擲";
                    if (job == 2) dropTitle = ((isCN) ? "丹药/" : "丹藥/") + dropTitle;
                    int currentDropInterval = common.getScore("pcub_drop_interval", targetName);
                    int currentEnCont = common.getScore("pcub_enable_continuous", targetName);
                    optForm
                        .dropdown(dropTitle + ((isCN) ? " （可能受网络延迟影响）" : " （可能受網路延遲影響）"), currentEnCont, "pcub.combat_option.disable", "pcub.combat_option.enable", "pcub.combat_option.disable_bedrock", "pcub.combat_option.disable_mobile")
                        .slider(dropTitle + ((isCN) ? "间隔 （秒）" : "間隔 （秒）"), 0F, 1F, 0.05F, (float) currentDropInterval / 20);
                    int currentFastSkill,currentSkillDuration;
                    //战士专项
                    if (job == 0) {
                        //按住潜行发动技能&发动技能所需时长
                        currentFastSkill = common.getScore("pcub_enable_fastskill", targetName);
                        String skillTitle = (isCN) ? "按住潜行发动技能" : "按住潛行發動技能";
                        currentSkillDuration = common.getScore("pcub_fastskill_duration", targetName);
                        optForm
                            .dropdown(skillTitle, currentFastSkill, "pcub.combat_option.disable", "pcub.combat_option.enable", "pcub.combat_option.enable_bedrock", "pcub.combat_option.enable_mobile")
                            .slider(skillTitle + ((isCN) ? " 所需时长 （秒）" : " 所需時長 （秒）"), 0F, 1F, 0.05F, (float) currentSkillDuration / 20);
                    } else currentFastSkill = currentSkillDuration = -1;
                    optForm
                        .closedResultHandler(() -> new BukkitRunnable(){
                                @Override
                                public void run() {
                                    targetPlayer.performCommand("forms open menubook-be");
                                }
                            }.runTask(main)
                        )
                        .validResultHandler(response -> new BukkitRunnable(){
                            @Override
                            public void run() {
                                //武器激活位置
                                if (job != 2) {
                                    int targetSlot = Integer.parseInt(response.next().toString()) + 1;
                                    if (targetSlot == 10) targetSlot = -1;
                                    if (common.getScore("WeaponSlot", targetName) != targetSlot) common.setScore("SlotSet", targetName, targetSlot);
                                }
                                //连续投掷
                                int enCont = Integer.parseInt(response.next().toString());
                                if (enCont != currentEnCont) {
                                    common.setScore("pcub_enable_continuous", targetName, enCont);
                                    String[] setResult = (isCN) ? new String[]{"始终禁用", "始终启用", "仅基岩版禁用", "仅 Android/iOS/WP 禁用"} : new String[]{"始終禁用", "始終啟用", "僅 Bedrock 版禁用", "僅 Android/iOS/WP 禁用"};
                                    sender.sendMessage("§a" + ((isCN) ? "连续投掷 已设置为" : "連續投擲 已設定為") + ": §b" + setResult[enCont]);
                                }
                                //每次投掷间隔
                                String strDropInterval = response.next().toString();
                                if (strDropInterval.length() > 4)
                                    strDropInterval = strDropInterval.substring(0, 4);
                                int dropSpeed = (int) (Float.parseFloat(strDropInterval) * 20);
                                if (dropSpeed != currentDropInterval) {
                                    common.setScore("pcub_drop_interval", targetName, dropSpeed);
                                    sender.sendMessage("§a" + ((isCN) ? "连续投掷间隔 已设置为" : "連續投擲間隔 已設定為") + ": §b" + strDropInterval + " 秒");
                                }
                                //战士专项
                                if (job == 0) {
                                    //按住潜行发动技能
                                    int fastSkill = Integer.parseInt(response.next().toString());
                                    if (fastSkill != currentFastSkill) {
                                        common.setScore("pcub_enable_fastskill", targetName, fastSkill);
                                        String[] setResult = (isCN) ? new String[]{"始终禁用", "始终启用", "仅基岩版启用", "仅 Android/iOS/WP 启用"} : new String[]{"始終禁用", "始終啟用", "僅 Bedrock 版啟用", "僅 Android/iOS/WP 啟用"};
                                        sender.sendMessage("§a" + ((isCN) ? "按住潜行发动技能 已设置为" : "按住潛行發動技能 已設定為") + ": §b" + setResult[fastSkill]);
                                    }
                                    //发动技能所需时长
                                    String strSkillDuration = response.next().toString();
                                    if (strSkillDuration.length() > 4)
                                        strSkillDuration = strSkillDuration.substring(0, 4);
                                    int skillDuration = (int) (Float.parseFloat(strSkillDuration) * 20);
                                    if (skillDuration != currentSkillDuration) {
                                        common.setScore("pcub_fastskill_duration", targetName, skillDuration);
                                        sender.sendMessage("§a" + ((isCN) ? "潜行技能所需时长 已设置为" : "潛行技能所需時長 已設定為") + ": §b" + strSkillDuration + " 秒");
                                    }
                                }
                            }
                        }.runTaskAsynchronously(main));
                    if(common.floodgateValid) common.floodgateApi.getPlayer(targetIDN).sendForm(optForm);
                    else common.geyserApi.sendForm(targetIDN, optForm);
                }
            }.runTaskAsynchronously(main);
        }
        else {
            String msg = "§6§l" + ((isCN) ? "盘灵无界功能专用命令" : "盤靈無界功能專用命令") + "§r\n所有用法：";
            if (args.length >= 1) msg = ((isCN) ? "§c错误: 不正确的参数" : "§c錯誤: 不正確的引數") + "。§r\n" + msg;
            String drop = (isCN) ? "丹药/退火符/击退符 " : "丹藥/退火符/擊退符 ";
            String tick = (isCN) ? " (单位为刻，20刻=1秒)" : " (單位為刻，20刻=1秒)";
            String fastSkill = (isCN) ? "[战] 按住潜行发动技能" : "[戰] 按住潛行發動技能";
            sender.sendMessage(msg + "\n/pcub dropContinuous §7<§rdisable§7|§renable§7|§rbedrockOnly§7|§rmobileOnly§7> — " + drop + ((isCN) ? "连续投掷" : "連續投擲") + "§r\n/pcub dropInterval §7<§r0§7-§r20§7> — " + drop + ((isCN) ? "连续投掷间隔" : "連續投擲間隔") + tick + "§r\n/pcub fastSkill §7<§rdisable§7|§renable§7|§rbedrockOnly§7|§rmobileOnly§7> — " + fastSkill + "§r\n/pcub fastSkillDuration §7<§r0§7-§r20§7> — " + fastSkill + ((isCN) ? " 所需时长" : " 所需時長") + tick + "§r\n/pcub stack§7 — " + ((isCN) ? "强制叠放背包中的所有物品，上限为 64" : "強制疊放揹包中的所有物品，上限為 64") + "§r\n(" + ((isCN) ? "所有命令都不区分大小写，留空最后一个参数可获取当前值，可以Tab补全)" : "所有命令都不區分大小寫，留空最後一個引數可獲取當前值，可以Tab補全)"));
        }
        return true;
    }

    // 提供武器激活位的文本名称
    int strIs;
    public String mst(String[] str){
        strIs ++;
        if (strIs == 10) return str[11];
        else return str[0] + str[strIs + 1] + str[1];
    }

    //命令补全
    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command command, String string, String[] args) {
        //Player targetPlayer = (Player) sender;
        //String targetName = targetPlayer.getName();
        ArrayList<String> content = new ArrayList<>();
        if(!(sender instanceof Player)) {
            //控制台
            for (String tab : (
                (args.length == 1) ? new String[]{"debug", "legacyStack"} :
//                (args.length == 2) ? new String[]{"true", "false"} :
                new String[]{}
            )) if (tab.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) content.add(tab);
            return content;
        } else if (args.length == 1) {
            String[] enTab = {"stack", "dropContinuous", "dropInterval", "fastSkill", "fastSkillDuration"};
            for (String tab : enTab) if (tab.toLowerCase().startsWith(args[0].toLowerCase())) content.add(tab);
            return content;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("dropContinuous") || args[0].equalsIgnoreCase("fastSkill")) {
                String[] enTab = {"disable", "enable", "bedrockOnly", "mobileOnly"};
                for (String tab : enTab) if (tab.toLowerCase().startsWith(args[1].toLowerCase())) content.add(tab);
                return content;
            }
            if (args[0].equalsIgnoreCase("dropInterval")) {
                if (args[1].isEmpty()) for (int i = 0; i <= 20; i += 4) content.add(i + "");
                else for (int i = 0; i <= 20; i ++) if ((i + "").startsWith(args[1])) content.add(i + " ");
                return content;
            }
            if (args[0].equalsIgnoreCase("fastSkillDuration")) {
                if (args[1].isEmpty()) for (int i = 0; i <= 20; i += 5) content.add(i + "");
                else for (int i = 0; i <= 20; i ++) if ((i + "").startsWith(args[1])) content.add(i + " ");
                return content;
            }
        }
        return null;
    }



    // 构造
    public CommandExecuter(Common common) {
        this.common = common;
        this.main = common.main;
    }
}
