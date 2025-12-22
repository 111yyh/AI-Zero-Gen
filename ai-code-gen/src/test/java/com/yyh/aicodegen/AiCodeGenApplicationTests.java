package com.yyh.aicodegen;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class AiCodeGenApplicationTests {

    public static void main(String[] args) throws InterruptedException {
                var q = new TaskQueue();
                var ts = new ArrayList<Thread>();
                for (int i=0; i<5; i++) {
                    var t = new Thread(() -> {
                        // 执行task:
                        while (true) {
                            try {
                                String s = q.getTask();
                                System.out.println("execute task: " + s);
                            } catch (InterruptedException e) {
                                return;
                            }
                        }
                    });
                    t.start();
                    ts.add(t);
                }
                var add = new Thread(() -> {
                    for (int i=0; i<10; i++) {
                        // 放入task:
                        String s = "t-" + Math.random();
                        System.out.println("add task: " + s);
                        q.addTask(s);
                        try { Thread.sleep(100); } catch(InterruptedException e) {}
                    }
                });
                add.start();
                add.join();
                Thread.sleep(100);
                for (var t : ts) {
                    t.interrupt();
                }
            }
        }

class TaskQueue {
    Queue<String> queue = new LinkedList<>();
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void addTask(String s) {
        lock.lock();
        try {
            this.queue.add(s);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String getTask() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            return queue.remove();
        } finally {
            lock.unlock();
        }
    }
}