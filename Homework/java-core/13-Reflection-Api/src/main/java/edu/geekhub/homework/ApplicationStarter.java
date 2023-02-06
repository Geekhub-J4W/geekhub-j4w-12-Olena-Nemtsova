package edu.geekhub.homework;

import edu.geekhub.homework.inject.InjectProcessor;

public class ApplicationStarter {

    public static void main(String[] args) throws IllegalAccessException {
        GeekHubCourse geekHubCourse = new GeekHubCourse();
        InjectProcessor injectProcessor = new InjectProcessor();
        injectProcessor.process(geekHubCourse);

        System.out.println(geekHubCourse);
    }
}
