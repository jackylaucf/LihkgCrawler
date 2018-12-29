package hk.com.sagetech.lihkgcrawler.jpa;

import java.io.Serializable;

class UserActivityCompositeKey implements Serializable {
    private int threadId;
    private int commentNumber;
}
