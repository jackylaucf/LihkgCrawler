package hk.com.sagetech.lihkgcrawler;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public final class Main {

    private static int mLower = 0;
    private static int mUpper = 0;

    public static void main(String[] args){
        initUi();
        try {
            Crawler.crawl(mLower, mUpper);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
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
