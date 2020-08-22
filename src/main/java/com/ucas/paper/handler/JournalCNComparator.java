package com.ucas.paper.handler;

import com.ucas.paper.entities.JournalCN;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/*
 * 排序类
 */
public class JournalCNComparator implements Comparator<JournalCN> {

    @Override
    public int compare(JournalCN journal1, JournalCN journal2) {
        int flag = 0;

        Collator com = Collator.getInstance(java.util.Locale.CHINA);


        //按FMS等级排序（A+, A,B,C,D,other)
        if (0 == flag && !journal1.getFms().contains("T")) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("A+", 1);
            map.put("A", 2);
            map.put("B", 3);
            map.put("C", 4);
            map.put("D", 5);

            String tmp1 = journal1.getFms().toUpperCase();
            String tmp2 = journal2.getFms().toUpperCase();
            Integer t1;
            Integer t2;
            t1 = map.getOrDefault(tmp1, 10);
            t2 = map.getOrDefault(tmp2, 10);
            flag = t1 - t2;
        }


        //FMS等级用TX排序，T和A排序不会同时出现
        if (0 == flag && journal1.getFms().contains("T")) {
            Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("T1", 1);
            map.put("T2", 2);
            map.put("T3", 3);
            map.put("T4", 4);
            map.put("T5", 5);
            map.put("T6", 6);
            map.put("T7", 7);
            map.put("T8", 8);
            map.put("T9", 9);

            String tmp1 = journal1.getFms().toUpperCase();
            String tmp2 = journal2.getFms().toUpperCase();
            Integer t1;
            Integer t2;
            t1 = map.getOrDefault(tmp1, 10);
            t2 = map.getOrDefault(tmp2, 10);
            flag = t1 - t2;
        }


        //再按照期刊名称排序
        if (flag == 0) {
            flag = com.getCollationKey(journal1.getName()).compareTo(com.getCollationKey(journal2.getName()));
        }

        if (flag > 0) {
            flag = 1;
        }
        if (flag < 0) {
            flag = -1;
        }
        return flag;
    }
}
