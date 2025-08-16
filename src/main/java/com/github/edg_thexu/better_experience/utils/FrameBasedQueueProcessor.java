package com.github.edg_thexu.better_experience.utils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 分帧队列处理器 - 用于分帧处理队列中的元素
 * @param <T> 队列元素类型
 */
public class FrameBasedQueueProcessor<T> {
    // 待处理队列
    private final Queue<T> processQueue = new LinkedList<>();
    // 每帧最大处理数量
    private int maxProcessPerFrame;
    // 队列为空时的回调函数
    private Runnable onQueueEmpty;
    // 元素处理器
    private Consumer<T> itemProcessor;
    // 队列填充器
    private Supplier<Boolean> queueFiller;
    // 帧计数器（可选）
    private int frameCounter = 0;

    /**
     * 创建分帧处理器
     * @param maxProcessPerFrame 每帧最大处理数量
     * @param itemProcessor 元素处理逻辑
     * @param queueFiller 队列填充逻辑（返回true表示有新数据）
     * @param onQueueEmpty 队列空时的回调
     */
    public FrameBasedQueueProcessor(int maxProcessPerFrame, 
                                   Consumer<T> itemProcessor, 
                                   Supplier<Boolean> queueFiller,
                                   Runnable onQueueEmpty) {
        this.maxProcessPerFrame = maxProcessPerFrame;
        this.itemProcessor = itemProcessor;
        this.queueFiller = queueFiller;
        this.onQueueEmpty = onQueueEmpty;
    }

    /**
     * 设置每帧处理数量
     * @param maxProcessPerFrame 最大处理数量
     */
    public void setMaxProcessPerFrame(int maxProcessPerFrame) {
        this.maxProcessPerFrame = maxProcessPerFrame;
    }

    /**
     * 添加单个元素到队列
     * @param item 待处理元素
     */
    public void enqueue(T item) {
        processQueue.offer(item);
    }

    /**
     * 批量添加元素到队列
     * @param items 元素集合
     */
    public void enqueueAll(Collection<? extends T> items) {
        processQueue.addAll(items);
    }

    /**
     * 执行单帧处理
     * @return 本帧实际处理的数量
     */
    public int processFrame() {
        // 如果队列为空，尝试填充
        if (processQueue.isEmpty()) {
            boolean hasNewData = queueFiller.get();
            if (!hasNewData) {
                onQueueEmpty.run();
                return 0;
            }
        }

        // 处理当前帧
        int processedCount = 0;
        for (int i = 0; i < maxProcessPerFrame && !processQueue.isEmpty(); i++) {
            T item = processQueue.poll();
            try {
                itemProcessor.accept(item);
                processedCount++;
            } catch (Exception e) {
                // 异常处理（根据需求自定义）
                System.err.println("Error processing item: " + e.getMessage());
            }
        }
        
        frameCounter++;
        return processedCount;
    }

    /**
     * 获取队列当前大小
     */
    public int getQueueSize() {
        return processQueue.size();
    }

    /**
     * 重置处理器（清空队列和计数器）
     */
    public void reset() {
        processQueue.clear();
        frameCounter = 0;
    }
}