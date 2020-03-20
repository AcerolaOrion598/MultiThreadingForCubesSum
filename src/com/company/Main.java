package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        System.out.print("Количество чисел: ");
        Scanner scanner = new Scanner(System.in);

        int arrayLength = scanner.nextInt();
        final int arrayQuarter = arrayLength / 4;

        ArrayList<Integer> nums = new ArrayList<>();

        for (int i = 0; i < arrayLength; i++) {
            nums.add(1 + (int) (Math.random() * 10));
        }

        System.out.print("Ассинхронно? ");
        scanner = new Scanner(System.in);

        if (scanner.nextLine().equals("y")) {
            asyncCounter(nums, arrayLength, arrayQuarter, System.currentTimeMillis());
        } else {
            syncCounter(nums, System.currentTimeMillis());
        }
    }

    private static void asyncCounter(List<Integer> nums, int arrayLength, int arrayQuarter, long startTime) throws InterruptedException {
        int answer = 0;
        List<MyThread> threads = new ArrayList<>();
        threads.add(new MyThread(nums.subList(0, arrayQuarter)));
        threads.add(new MyThread(nums.subList(arrayQuarter, arrayQuarter * 2)));
        threads.add(new MyThread(nums.subList(arrayQuarter * 2, arrayQuarter * 3)));
        threads.add(new MyThread(nums.subList(arrayQuarter * 3, arrayLength)));

        for (MyThread thread : threads) {
            thread.start();
        }

        for (MyThread thread : threads) {
            thread.join();
        }

        List<Integer> answerList = new ArrayList<>();

        for (MyThread thread : threads) {
            answerList.add(thread.getCubesSum());
        }

        for (Integer cubesSum : answerList) {
            answer += cubesSum;
        }

        System.out.println("Ответ: " + answer);
        System.out.println("Затраченное время: " + (System.currentTimeMillis() - startTime));
    }

    private static void syncCounter(List<Integer> nums, long startTime) {
        System.out.println("Ответ: " + countCubesSum(nums));
        System.out.println("Затраченное время: " + (System.currentTimeMillis() - startTime));
    }

    private static Integer countCubesSum(List<Integer> list) {
        int answer = 0;

        for (int i = 0; i < 10000; i++) { //Считаем 10000 раз просто чтобы нагрузить процессор
            answer = 0;

            for (Integer integer : list) {
                answer += integer * integer * integer;
            }
        }

        return answer;
    }

    static class MyThread extends Thread {
        private List<Integer> list;
        int cubesSum;

        MyThread(List<Integer> list) {
            this.list = list;
        }

        int getCubesSum() {
            return cubesSum;
        }

        @Override
        public void run() {
            super.run();
            cubesSum = Main.countCubesSum(list);
        }
    }
}
