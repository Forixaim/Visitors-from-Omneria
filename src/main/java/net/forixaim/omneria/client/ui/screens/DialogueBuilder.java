package net.forixaim.omneria.client.ui.screens;


import net.forixaim.omneria.client.ui.screens.components.dialogue.ComponentBuilder;
import net.forixaim.omneria.client.ui.screens.components.dialogue.OptionComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 用多叉树来优化流式对话框（我自己起的名词，就是没有多个分支几乎都是一条直线的对话，不过好像带有分支的也可以用？
 * 如果要构建树状对话就手动设置answerRoot即可
 * 从Command中得到启发{@link net.minecraft.commands.Commands}
 * @author LZY
 */
public class DialogueBuilder
{

    protected DialogueScreen screen;//封装一下防止出现一堆杂七杂八的方法
    private TreeNode answerRoot;
    private TreeNode answerNode;
    private final EntityType<?> entityType;
    public DialogueBuilder(Entity entity) {
        screen = new DialogueScreen(entity, entity.getType());
        this.entityType = entity.getType();
        init();
    }
    public DialogueBuilder(Entity entity, EntityType<?> entityType) {
        screen = new DialogueScreen(entity, entityType);
        this.entityType = entityType;
        init();
    }

    public boolean isEmpty(){
        return answerRoot == null;
    }

    /**
     * 用于构建树状对话
    * */
    public void setAnswerRoot(TreeNode root){
        this.answerRoot = root;
    }

    /**
     * 重写这个是为了让你记得这才是Screen真正被调用的初始化的地方。建议在这里作些判断再调用start。
    * */
    public DialogueBuilder init() {
        return this;
    }

    /**
     * 初始化对话框，得先start才能做后面的操作
     * @param greeting 初始时显示的话
     */
    public DialogueBuilder start(Component greeting){
        answerRoot = new TreeNode(greeting);
        answerNode = answerRoot;
        return this;
    }

    public DialogueBuilder start(int greeting){
        return start(ComponentBuilder.BUILDER.buildDialogueAnswer(entityType,greeting));
    }

    public DialogueBuilder addFinalChoice(Component finalOption, long returnValue){
        if(answerNode == null)
            return null;
        answerNode.addChild(new TreeNode.FinalNode(finalOption, returnValue));
        return this;
    }

    public DialogueBuilder addFinalChoice(int finalOption, long returnValue){
        return addFinalChoice(ComponentBuilder.BUILDER.buildDialogueOption(entityType,finalOption), returnValue);
    }

    /**
     * 添加选项进树并返回下一个节点
     * @param option 该选项的内容
     * @param answer 选择该选项后的回答内容
     */
    public DialogueBuilder addChoice(Component option, Component answer){
        if(answerNode == null)
            return null;
        answerNode.addChild(answer,option);

        //直接下一个
        List<TreeNode> list = answerNode.getChildren();
        if(!(list.size() == 1 && list.get(0) instanceof TreeNode.FinalNode)){
            answerNode = list.get(0);
        }

        return this;
    }

    /**
     * 使用BUILDER构建
     * 添加选项进树并返回下一个节点
     * @param option 该选项的内容编号
     * @param answer 选择该选项后的回答内容编号
     */
    public DialogueBuilder addChoice(int option, int answer){
        return addChoice(ComponentBuilder.BUILDER.buildDialogueOption(entityType,option), ComponentBuilder.BUILDER.buildDialogueAnswer(entityType,answer));
    }

    /**
     * 按下按钮后执行
     */
    public DialogueBuilder thenExecute(Runnable runnable){
        if(answerNode == null)
            return null;
        answerNode.execute(runnable);
        return this;
    }
    /**
     * 按下按钮后执行。记得在handle的时候不要把玩家设置为null，提前返回，否则可能中断对话！
     */
    public DialogueBuilder thenExecute(long returnValue){
        answerNode.execute(returnValue);
        return this;
    }

    /**
     * 根据树来建立套娃按钮
     */
    public DialogueScreen build(){
        if(answerRoot == null)
            return screen;
        else
        {
            screen.setDialogueAnswer(answerRoot.getAnswer());
            List<OptionComponent> choiceList = new ArrayList<>();
            assert answerRoot.getChildren() != null;
            for(TreeNode child : answerRoot.getChildren()){
                choiceList.add(new OptionComponent(child.getOption().copy(), createChoiceButton(child)));
            }
            screen.setupDialogueChoices(choiceList);
            return screen;
        }

    }

    /**
     * 递归添加按钮。放心如果遇到没有添加选项的节点会自动帮你添加一个返回空内容返回值为0的FinalNode。
     */
    private Button.OnPress createChoiceButton(TreeNode node){

        if(node instanceof TreeNode.FinalNode finalNode){
            return button -> {
                screen.finishChat(finalNode.getReturnValue());
                if(finalNode.canExecute()){
                    finalNode.execute();
                }
            };
        }

        if (node instanceof TreeNode.ActionNode actionNode){
            return button -> {
                if (actionNode.canExecute()){
                    actionNode.execute();
                }
            };
        }

        //否则继续递归创建按钮
        return button -> {
            if(node.canExecute()){
                node.execute();
            }
            if(node.canExecuteCode()){
                if(node.getExecuteValue() == 0){
                    throw new IllegalArgumentException("The return value '0' is for ESC");
                }
                screen.execute(node.getExecuteValue());
            }
            screen.setDialogueAnswer(node.getAnswer());
            List<OptionComponent> choiceList = new ArrayList<>();
            List<TreeNode> options = node.getChildren();
            if(options == null){
                options = new ArrayList<>();
                options.add(new TreeNode.FinalNode(Component.empty(),Long.MIN_VALUE));
            }
            for(TreeNode child : options){
                choiceList.add(new OptionComponent(child.getOption().copy(), createChoiceButton(child)));
            }
            screen.setupDialogueChoices(choiceList);
        };
    }

}