package com.chris.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import sun.text.normalizer.Trie;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YaoQi on 2017/2/21.
 */
@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
    private static final String DEFAULT_REPLACEMENT = "敏感词";
    //Build a prefix tree
    private class TrieNode {
        //Is the end of the sensitive word
        private boolean end = false;
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        /*
        *向指定位置添加节点树
         */
        void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }
        //获取下一个节点
        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        //判断是否为最后一个节点
        boolean isKeywordEnd() {
            return end;
        }

        //设置该字符是否在单词的最后一个位置
        void setKeywordEnd(boolean end) {
            this.end = end;
        }

        //得到下个节点的数量
        int getSubNodeCount() {
            return subNodes.size();
        }

    }

    //Initialize
    private TrieNode rootNode = new TrieNode();

    //add word to the trie
    private void addWord(String lineTxt) {
        if (lineTxt == null || lineTxt.isEmpty()) {
            return;
        }
        TrieNode tempNode = rootNode;

        for(int i = 0; i < lineTxt.length(); i++) {
            Character c = lineTxt.charAt(i);
            TrieNode node = tempNode.getSubNode(c);

            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;

            //To the end of the word
            if (i == lineTxt.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    //Initializing the trie when starting the web project
    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode = new TrieNode();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(reader);
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                addWord(lineTxt.trim());
            }
            reader.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }

    }

    //判断是否是一个符号
    private boolean isSymbol(char c) {
        int ic = (int)c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String replacement = DEFAULT_REPLACEMENT;
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;

        //to store the filtered text
        StringBuilder result = new StringBuilder();
        while (position < text.length()) {
            char c = text.charAt(position);
            //Skip white chars
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            tempNode = tempNode.getSubNode(c);
            //the match ends
            if (tempNode == null) {
                result.append(text.charAt(begin));
                begin = begin + 1;
                position = begin;
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                //find a sensitive word
                result.append(replacement);
                begin = position + 1;
                position = begin;
                tempNode = rootNode;
            } else {
                position++;
            }
        }
        result.append(text.substring(begin));
        return result.toString();
    }
    //test filter() method
/*    public static void main(String[] args) {
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("好色");
        System.out.println(s.filter("你好*色^^情XX"));
    }*/
}
