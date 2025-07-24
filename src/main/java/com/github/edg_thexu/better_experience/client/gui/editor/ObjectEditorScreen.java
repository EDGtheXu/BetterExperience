package com.github.edg_thexu.better_experience.client.gui.editor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.gui.widget.ScrollPanel;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@OnlyIn(Dist.CLIENT)
public class ObjectEditorScreen extends Screen{
    private final ObjectEditor objectEditor;
    private final List<AbstractWidget> widgets = new ArrayList<>();
    private final int widgetWidth = 200;
    private final int widgetHeight = 22;
    private final int padding = 5;
    Button back;

    ScrollPanel panel;
    Object data;

    Stack<ObjectEditor> editorStack = new Stack<>();
    StringWidget navigation;

    public ObjectEditorScreen(Component title) {
        super(title);

        ItemStack stack = Minecraft.getInstance().player == null? ItemStack.EMPTY : Minecraft.getInstance().player.getOffhandItem();
        this.data = stack;

        this.objectEditor = new ObjectEditor(data, (obj)->{
            System.out.println(obj.toString());
            // 根节点保存时
            if(this.jsonEditor == null){
                this.createJsonEditor();
            }else{
                this.jsonEditor.setError();
//                this.removeWidget(this.jsonEditor.textField);
                this.jsonEditor = null;
            }

        }, this, ItemStack.class);


    }

    @Override
    protected void init() {
        super.init();
        this.editorStack.clear();

        this.back = Button.builder(Component.literal("<-"), (b) -> {
            if(editorStack.size() > 1){
                editorStack.pop();
                this.openObjectEditor(editorStack.peek());
                ObjectEditor editor = editorStack.pop();
                editor.getPropertyEditors().forEach(PropertyEditor::clear);

                navigation.setMessage(Component.literal(updateNavigation()));
            }else{
                this.createJsonEditor();
            }
        }).bounds(0, 20, 30, 20).build();

        this.openObjectEditor(objectEditor);
    }

    public void openObjectEditor(ObjectEditor parent){
        this.clearWidgets();
        this.addRenderableWidget(back);
        editorStack.push(parent);
        this.widgets.clear();

        int x = width / 2;
        int y = 50;
        // 为每个属性创建编辑器
        for (PropertyEditor editor : parent.getPropertyEditors()) {
            // 添加标签
//            Button label = Button.builder(Component.literal(editor.getFieldName() + ":"), (b) -> {})
//                    .bounds(0, y, 100, widgetHeight)
//                    .tooltip(Tooltip.create(Component.literal(editor.getField().getType().getName())))
//                    .build();
            StringWidget label = new StringWidget(0, y, 100, widgetHeight, Component.literal(editor.getFieldName() + ":"), font);
            label.setTooltip(Tooltip.create(Component.literal(editor.getField().getType().getName())));
//
//            this.addRenderableWidget(label);

            // 添加编辑器控件
            AbstractWidget widget = editor.createWidget(110, y, 100, value -> {
                // 实际值更新由各个编辑器内部处理
            });
//            this.addRenderableWidget(widget);
//            widgets.add(widget);
            this.widgets.add(label);
            this.widgets.add(widget);
            y += widgetHeight + padding;
        }
        // 添加保存按钮
        this.addRenderableWidget(Button.builder(Component.literal("Save"), (b) -> {
            parent.saveChanges();
//            Minecraft.getInstance().displayGuiScreen(null);
        }).bounds(70, 20, 30, 20).build());
        String path = updateNavigation();
        navigation = new StringWidget(minecraft.screen.width, 20, Component.literal(path), font);
        this.addRenderableWidget(navigation);
//        g.drawString(font, "Object Editor: " + path, 5, 1, 0xFFFFFF);

        this.panel = new ScrollPanel(minecraft, this.width / 2, widgets.size() / 2 * (widgetHeight + padding) + 5, 50,0, 1){

            @Override
            public void updateNarration(NarrationElementOutput narrationElementOutput) {

            }

            @Override
            public NarrationPriority narrationPriority() {
                return NarrationPriority.NONE;
            }

            @Override
            protected int getContentHeight() {
                int size = widgets.size() / 2;
                return size * (widgetHeight + padding);
            }

            @Override
            protected void drawPanel(GuiGraphics guiGraphics, int i, int i1, Tesselator tesselator, int i2, int i3) {
                for (AbstractWidget widget : widgets) {
                    widget.render(guiGraphics, i2,i3, minecraft.getTimer().getGameTimeDeltaPartialTick(true));

                }
            }

            @Override
            public boolean mouseScrolled(double p_94686_, double p_94687_, double p_94688_, double p_294830_) {

                boolean flag = super.mouseScrolled(p_94686_, p_94687_, p_94688_, p_294830_);
                if (flag) {
                    int c = 0;
                    for (AbstractWidget widget : widgets) {
                        widget.setY((int) (-this.scrollDistance + ( 2 + c / 2 ) * (widgetHeight + padding)) );
                        c++;
                    }
                }
                return flag;
            }

            public List<? extends GuiEventListener> children() {
                return widgets;
            }
        };

        this.addRenderableWidget(panel);
    }

    public void addEditor(MultiLineEditBox editor){
//        if(this.jsonEditor != null){
//            this.removeEditor(this.jsonEditor);
//        }

        this.addRenderableWidget(editor);
    }

    public void addWidget(AbstractWidget widget){
        this.addRenderableWidget(widget);
    }

    public void deleteWidget(AbstractWidget widget){
        this.removeWidget(widget);
    }

    public void removeEditor(MultiLineEditBox editor){
        this.removeWidget(editor);
    }

    private String updateNavigation(){
        return editorStack.stream().map(o->o.getTargetClass().getSimpleName()).reduce((a, b) -> a + " -> " + b).orElse("");
    }


    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTicks) {
        renderBackground(g, mouseX, mouseY, partialTicks);
        super.render(g, mouseX, mouseY, partialTicks);

        // 绘制标题
        g.drawCenteredString(font, title.getString(), width / 2, 20, 0xFFFFFF);
    }

    static class JsonEditor {

        MultiLineEditBox textField;
        MultiLineEditBox errorField;
        ObjectAdapter.ObjectData data;
        ObjectEditorScreen screen;
        Object value;
        public JsonEditor(ObjectEditorScreen screen, ObjectAdapter.ObjectData data, Object value){
            this.value = value;
            this.screen = screen;
            int x1 = screen.width /2 + 15;
            int w = screen.width - x1-5;
            String str = "";
            this.textField = new MultiLineEditBox(screen.getMinecraft().font,
                    x1, 20, w, 130
                    ,Component.literal("Object Data:"),Component.literal(str));
            this.data = data;
            if(value != null){
                var d = data.codec().encodeStart(Minecraft.getInstance().level.registryAccess().createSerializationContext(
                        JsonOps.INSTANCE), value).result().orElse(null);
                if(d!= null){
                    if(d instanceof JsonObject object){
                        Gson gson = new Gson().newBuilder().setPrettyPrinting().create();
                        str = gson.toJson(object);
                    }else {
                        str = d.toString();
                    }
                    textField.setValue(str);
                }
            }
        }

        public ObjectEditorScreen getScreen() {
            return screen;
        }

        public boolean setError(){
            String str = textField.getValue();
            var data = ObjectAdapter.getObjectData(this.value.getClass());
            int x = getScreen().width /2 + 15;
            int w = getScreen().width - x-5;

            if(data!= null){
                try {
                    var res = data.codec().parse(Minecraft.getInstance().level.registryAccess()
                            .createSerializationContext(JsonOps.INSTANCE), JsonParser.parseString(str));
                    if(res.result().isPresent()){
                        this.value = res.result().get();
//                        this.button.setMessage(Component.literal(this.value.toString()));
                    }else{
                        if(this.errorField != null){
                            this.getScreen().removeEditor(errorField);
                        }
                        this.errorField = new MultiLineEditBox(getScreen().getMinecraft().font,
                                x, 160, w, 40
                                ,Component.literal("Error:"),Component.literal(res.error().toString()));
                        this.errorField.setValue(res.error().toString());
                        this.getScreen().addEditor(errorField);
                        return true;
                    }
                }catch (Exception e){
                    if(this.errorField != null){
                        this.getScreen().removeEditor(errorField);
                    }
                    this.errorField = new MultiLineEditBox(getScreen().getMinecraft().font,
                            x, 160, w, 40
                            ,Component.literal("Error:"),Component.literal(e.getMessage()));
                    this.errorField.setValue(e.getMessage());
                    this.getScreen().addEditor(errorField);
                    return true;
                }

            }
            if(this.errorField != null){
                this.getScreen().removeEditor(errorField);
            }
//            this.editor.setSelectedProperty(null);
            this.getScreen().removeEditor(textField);
            this.textField = null;
            return false;
        }


    }

    public JsonEditor jsonEditor;
    private void createJsonEditor(){
        this.jsonEditor = new JsonEditor(this, ObjectAdapter.getObjectData(ItemStack.class),this.data);
        this.addEditor(jsonEditor.textField);
    }

    public void removeJsonEditor(){
        if(this.jsonEditor != null){
            this.removeEditor(this.jsonEditor.textField);
            this.jsonEditor = null;
        }
    }

}