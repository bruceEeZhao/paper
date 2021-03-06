package com.ucas.paper.handler;

import com.ucas.paper.entities.Journal;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/*
 * 排序类
 */
public class JournalComparatorEng implements Comparator<Journal> {

    @Override
    public int compare(Journal journal1, Journal journal2) {
        int flag = 0;
        //先按学科名称排序

        Collator com = Collator.getInstance(java.util.Locale.CHINA);

        flag = com.getCollationKey(journal1.getSubject().getEngName()).compareTo(com.getCollationKey(journal2.getSubject().getEngName()));

        //再按FMS等级排序（A+, A,B,C,D,other)
        if (0 == flag) {
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
