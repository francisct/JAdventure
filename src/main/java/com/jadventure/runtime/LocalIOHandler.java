package com.jadventure.runtime;

import java.util.Scanner;

public class LocalIOHandler implements IOHandler {

    @Override
    public String getInput() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        return input;
    }

    @Override
    public void sendOutput(String output) {
        System.out.println(output);
    }

}
