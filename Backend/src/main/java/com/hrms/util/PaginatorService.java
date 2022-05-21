package com.hrms.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class PaginatorService {

    public int pageNum = 1;         // default page number is 0 (yes it is weird)
    public int pageSize = 5;        // default page size is 10
    public String sortField = "id";
    public String sortDir = "desc";

    HttpServletRequest request;
    Map<String, Object> clientParams;


    public PaginatorService(){
    }

    /**
     * @param page must not be less than zero.
     * @param size must not be less than one.
     */
    public PaginatorService(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size must not be less than one!");
        }
        this.pageNum = page;
        this.pageSize = size;
    }

    public PaginatorService( HttpServletRequest request, Map<String, Object> clientParams ) {
        this.request = request;
        this.clientParams = clientParams;

        if(this.clientParams.containsKey("page")){
            this.pageNum = Integer.parseInt(this.clientParams.get("pageNum").toString());
        }
        if(this.clientParams.containsKey("size")){
            this.pageSize = Integer.parseInt(this.clientParams.get("pageSize").toString());
        }

    }

    public PaginatorService(HttpServletRequest request){
        this.request = request;
        if (this.request.getParameter("pageNum") != null && !this.request.getParameter("pageNum").isEmpty()) this.pageNum = Integer.parseInt(this.request.getParameter("pageNum"));
        if (this.request.getParameter("pageSize") != null && !this.request.getParameter("pageSize").isEmpty()) this.pageSize = Integer.parseInt(this.request.getParameter("pageSize"));
        if (this.request.getParameter("sortField") != null && !this.request.getParameter("sortField").isEmpty()) this.sortField = this.request.getParameter("sortField");
        if (this.request.getParameter("sortDir") != null && !this.request.getParameter("sortDir").isEmpty()) this.sortDir = this.request.getParameter("sortDir");
    }

    public void setDefaultSortDir(String sortField, String sortDir){
        this.sortField = sortField;
        this.sortDir = sortDir;
    }

    public void setPage(int page){
        this.pageNum = page;
    }
    public void setSize(int size){
        this.pageSize = size;
    }
    public int getPage(){
        return this.pageNum;
    }
    public int getSize(){
        return this.pageSize;
    }
    public int getOffset(){
        return ((this.pageNum - 1) * this.pageSize);
    }



}
