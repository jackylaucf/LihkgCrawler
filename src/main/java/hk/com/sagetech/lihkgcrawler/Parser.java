package hk.com.sagetech.lihkgcrawler;

import hk.com.sagetech.lihkgcrawler.jpa.UserActivityModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

final class Parser {

    private static final String COMMENT_CLASS_NAME = "_2bokd4pLvU5_-Lc97NVqzn   _20q6NML4ErQq8usNV-npj5";
    private static final String USER_BAR_CLASS_NAME = "ZZtOrmcIRcvdpnW09DzFk";
    private static final String DATE_TIME_CLASS_NAME = "Ahi80YgykKo22njTSCzs_";
    private static final String CONTENT_CLASS_NAME = "GAagiRXJU88Nul1M7Ai0H";
    private static final String BLOCKQUOTE_CLASS_NAME = "_31B9lsqlMMdzv-FSYUkXeV";
    private static final String CONTENT_ATTRIBUTE_KEY = "data-ast-root";
    private static final String DATE_TIME_ATTRIBUTE_KEY = "data-tip";
    private static final String EMOJI_CONTAINER_ATTRIBUTE_KEY = "data-emoji-container";
    private static final String USER_HREF_PREFIX = "/profile/";

    private Parser(){
        //Empty private constructor
    }

    static List<UserActivityModel> getUserActivities(int threadId, String html){
        List<UserActivityModel> userActivities = new ArrayList<>();
        Document htmlDoc = Jsoup.parse(html);

        //Thread Title
        String threadTitle = null;
        Elements titleElement = htmlDoc.getElementsByTag("title");
        if(!titleElement.isEmpty()){
            threadTitle = titleElement.first().text().replace(" | LIHKG", "");
        }

        Elements commentElements = htmlDoc.getElementsByClass(COMMENT_CLASS_NAME);
        if(!commentElements.isEmpty()){
            for(Element commentElement: commentElements){
                //Comment Number
                int commentNumber = Integer.valueOf(commentElement.id());

                //Thread page
                int threadPage = (commentNumber%25==0) ? commentNumber/25 : (commentNumber/25+1);

                //UserID & UserName
                int userId = 0;
                String userName = null;
                Elements userElements = commentElement.getElementsByClass(USER_BAR_CLASS_NAME);
                if(!userElements.isEmpty()){
                    Element userElement = userElements.first().child(0);
                    String userIdStr = userElement.attr("href").replace(USER_HREF_PREFIX, "");
                    userId = Integer.valueOf(userIdStr);
                    userName = userElement.text();
                }

                //Comment DateTime
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年M月d日 hh:mm:ss");
                LocalDateTime dateTime = null;
                Elements dateTimeElements = commentElement.getElementsByClass(DATE_TIME_CLASS_NAME);
                if(!dateTimeElements.isEmpty()){
                    String dateTimeStr = dateTimeElements.first().attr(DATE_TIME_ATTRIBUTE_KEY);
                    try {
                        dateTime = formatter.parse(dateTimeStr)
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                //Comment Content
                String content = null;
                commentElement.select("span").remove();
                Elements contentElements = commentElement.getElementsByClass(CONTENT_CLASS_NAME);
                if(!contentElements.isEmpty()){
                    int childrenSize = contentElements.first().childNodeSize();
                    Node targetParentNode = null;
                    if(childrenSize>0){
                        if(childrenSize==1){
                            targetParentNode = contentElements.first().child(0);
                        }else{
                            for(Node node: contentElements.first().childNodes()){
                                if(node.hasAttr(CONTENT_ATTRIBUTE_KEY)){
                                    targetParentNode = node;
                                    break;
                                }
                            }
                        }

                        if(targetParentNode!=null){
                            int nodeSize = targetParentNode.childNodeSize();
                            StringBuilder buffer = new StringBuilder();
                            for (int i = 0; i < nodeSize; i++) {
                                Node node = targetParentNode.childNode(i);

                                if(node.hasAttr("class")){
                                    if(node.attr("class").equals(BLOCKQUOTE_CLASS_NAME)) {
                                        continue;
                                    }
                                }

                                if(node.hasAttr("alt")) {
                                    buffer.append(node.attr("alt"));
                                }else if(node.hasAttr("href")) {
                                    buffer.append(System.lineSeparator());
                                    buffer.append(node.attr("href"));
                                }else if(node.hasAttr(EMOJI_CONTAINER_ATTRIBUTE_KEY)){
                                    buffer.append(node.childNode(0).attr("alt"));
                                }else{
                                    buffer.append(node.toString().trim());
                                }
                            }
                            content = buffer.toString().replace("<br>", "");
                        }
                    }
                }

                //Build the UserActivityModel
                UserActivityModel userActivity = new UserActivityModel();
                userActivity.setThreadId(threadId);
                userActivity.setThreadTitle(threadTitle);
                userActivity.setThreadPage(threadPage);
                userActivity.setCommentNumber(commentNumber);
                userActivity.setUserId(userId);
                userActivity.setUserName(userName);
                userActivity.setDateTime(dateTime);
                userActivity.setContent(content);
                userActivities.add(userActivity);
            }
        }
        return userActivities;
    }
}
