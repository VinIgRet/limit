package ru.vinigret.limit.service;

import org.springframework.stereotype.Component;
import ru.vinigret.limit.urlico.uchetul.ListDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Component
public class PageModel {

    private static int PAGE = 0;

    private static int SIZE = 10;

    private HttpServletRequest request;

    public PageModel(HttpServletRequest request) {
        this.request = request;
    }

    public void initPageAndSize(ListDTO listDTO){
        initPageAndSize();
        if (listDTO.getP() != null && listDTO.getP() > 0) {
            PAGE = listDTO.getP() - 1;
        }
        if (listDTO.getN() != null && listDTO.getN() > 0) {
            SIZE = listDTO.getN();
        }
    }

    public void initPageAndSize(){
        int page = 0;
        int size = 0;
        if (request.getParameter("p") != null && !request.getParameter("p").isEmpty()) {
            PAGE = Integer.parseInt(request.getParameter("p")) - 1;
        }

        if (request.getParameter("n") != null && !request.getParameter("n").isEmpty()) {
            SIZE = Integer.parseInt(request.getParameter("n"));
        }
    }
    public static void setSIZE(int SIZE) {
        PageModel.SIZE = SIZE;
    }

    public static void setPAGE(int PAGE) {
        PageModel.PAGE = PAGE;
    }

    public static int getPAGE() {
        return PAGE;
    }

    public static int getSIZE() {
        return SIZE;
    }

    public static ArrayList<Integer> getPagList(int totalPage, int tekPage){
        ArrayList<Integer> listPag = new ArrayList<Integer>();
        int begin = tekPage - 2 >= 0 ? tekPage - 2 : 0;
        int end = begin + 4 <= totalPage - 1 ? begin + 4 : totalPage - 1;
        if (end == totalPage - 1 && begin > 0 && end - begin < 5){
            begin = end - 4 > 0 ? end - 4 : 0;
        }
        int countPrev = begin > 4 ? 5 : begin;
        int countAfter = totalPage - end >= 10 - countPrev ? 10 - countPrev : totalPage - end - 1;
        if (countAfter < 5 && begin > 4) {
            countPrev = totalPage - 5 - countAfter > begin ? (begin > totalPage - 5 ? totalPage - 5 : (begin >= 10 - countAfter ? 10 - countAfter : begin)) - countAfter : (begin >= 10 - countAfter ? 10 - countAfter : begin);
        }
        if (countPrev > 0) {
            listPag.add(1);
            if (begin < 0) {
                int korr = begin < 8 ? 1 : begin - 4;
                for (int i = 1; i < countPrev; i++) {
                    listPag.add(i + korr);
                }
            } else {
                int tekVal = begin / 2;
                for (int i = 1; i < countPrev; i++) {
                    while (tekVal + countPrev - i > begin) {
                        tekVal --;
                    }
                    listPag.add(begin - tekVal);
                    tekVal /= 3;
                    if (tekVal < 1) {
                        tekVal =1;
                    }
                }
                /*
                int step = begin / 2;
                if (begin - step < countPrev - 1) {
                    step =+ countPrev - 1;
                }
                int prevval = 1;
                for (int i = 0; i < countPrev - 1; i++) {
                    if (begin - step == prevval) {
                        step--;
                    }
                    prevval = begin - step;
                    listPag.add(prevval);
                    step = (step +1) / 2;
                }

                 */
            }
        }
        for (int i = begin; i <= end; i++) {
            listPag.add(i + 1);
        }
        if (countAfter > 0) {
            for (int i = 1; i < countAfter; i++) {
                listPag.add(-2);
            }
            listPag.add(totalPage);
        }
        return listPag;
    }
}