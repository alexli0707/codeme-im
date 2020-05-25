package org.codeme.im.imclient;

import org.codeme.im.imclient.task.runnable.ConsoleScannerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * IMClientApplication
 *
 * @author walker lee
 * @date 2020/5/15
 */
@SpringBootApplication(scanBasePackages = {"org.codeme"})
public class IMClientApplication implements CommandLineRunner {
    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(IMClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
        ConsoleScannerTask scan = new ConsoleScannerTask(applicationContext);
        Thread thread = new Thread(scan);
        thread.setName("scanner-thread");
        thread.start();
    }
}
