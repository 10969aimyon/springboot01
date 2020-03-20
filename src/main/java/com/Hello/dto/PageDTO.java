package com.Hello.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class PageDTO {
    // 页面总数
    private int totalPage;
    // 当前页面问题列表
    private List<QuestionDto> quesetions;
    // 当前页面
    private int currentPage;
    // 页面总数
    private List<Integer> pages = new ArrayList<>();
    // 判断页面内容
    private boolean showNextPage;
    private boolean showLastPage;
    private boolean showFirstPage;
    private boolean showEndPage;


    @Override
    public String toString() {
        return "PageDTO{" +
                "totalPage=" + totalPage +
                ", currentPage=" + currentPage +
                ", pages=" + pages +
                ", showNextPage=" + showNextPage +
                ", showLastPage=" + showLastPage +
                ", showFirstPage=" + showFirstPage +
                ", showEndPage=" + showEndPage +
                '}';
    }

    public void setPage(int page, int size, int totalCount){


        // 计算出总页数
        if (totalCount % size == 0){
            this.totalPage = totalCount / size;
        }else {
            this.totalPage = totalCount / size + 1;
        }

        if (page < 1){
            page = 1;
        }
        if (page > totalPage){
            page = totalPage;
        }

        // 将当前页面放入list中
        pages.add(page);
        // 设定当前页的值
        this.currentPage = page;

        // 设定页面总数
        this.totalPage = totalPage;



        // 计算出list中要显示的数据
        for (int i = 1 ; i < 3; i++){
            // 如果前（1，2）页不超过第一页，就加上
            if (currentPage - i >= 1){
                pages.add(currentPage - i);
            }
            // 如果后（1，2）页不超过最后一页，就加上
            if (currentPage + i <= totalPage ){
                pages.add( currentPage+i );
            }
        }

        Collections.sort(pages);
        // 判断showLastPage是否存在
        if (currentPage != 1){
            showLastPage = true;
        }else {
            showLastPage = false;
        }

        // 判断showNextPage是否存在
        if (currentPage != totalPage){
            showNextPage = true;
        }else {
            showNextPage = false;
        }

        // 判断showFirstPage是否存在
        if (pages.contains(1)){
            showFirstPage = false;
        } else {
            showFirstPage = true;
        }

        // 判断showEndPage是否存在
        if (pages.contains(totalPage)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }

    }
}
