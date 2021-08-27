package com.tanklab.mathless.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class FileTreeNode {
    private String name;
    private String path;
    //private Long length;
    private Boolean ifDir = false;//是否文件夹
    private List<FileTreeNode> children = new ArrayList<>();

    public void addChild(FileTreeNode treeNode) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(treeNode);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIfDir() {
        return ifDir;
    }

    public void setIfDir(Boolean ifDir) {
        this.ifDir = ifDir;
    }

    public List<FileTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<FileTreeNode> children) {
        this.children = children;
    }
}
