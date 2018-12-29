package hk.com.sagetech.lihkgcrawler;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class CommandLine implements CommandLineRunner {

    @Autowired
    private Crawler mCrawler;

    private static int mLower = 0;
    private static int mUpper = 0;

    @Override
    public void run(String... args) throws Exception {
        initUi();
        mCrawler.crawl(mLower, mUpper);
    }

    private static void initUi(){
        System.out.println("==============================================LIHKG Web Crawler================================================");
        System.out.println("Rule: You have to enter range of thread ID to activate the crawling process. The input ID must be positive integer");
        final Scanner scanner = new Scanner(System.in);
        boolean isValid;
        do{
            isValid = true;
            try{
                if(mLower==0){
                    System.out.print("Please input the lower bound of thread id: ");
                    mLower = scanner.nextInt();
                }
                System.out.print("Please input the upper bound of thread id: ");
                mUpper = scanner.nextInt();
            }catch(InputMismatchException e){
                scanner.nextLine();
                isValid = false;
                System.out.println("Invalid input. Please input integer value!");
                continue;
            }
            if(mLower>mUpper || mLower<=0){
                System.out.println("Invalid input.");
                isValid = false;
                mLower = 0;
            }
        }while(!isValid);
        scanner.close();
    }
}
