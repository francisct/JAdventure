package com.jadventure.runtime;

import java.util.Scanner;

public class LocalIOHandler implements IOHandler {

    // System.in is owned by the VM, which is responsible for closing it
    @SuppressWarnings("resource")
    @Override
    public String getInput() {
        Scanner scanner = new Scanner(System.in);    
        String input = scanner.nextLine();

        return input;
    }

    @Override
    public void sendOutput(String output) {
        System.out.println(output);
    }

}
