package com.enn.vo.energy.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "NodeTree")
public class NodeTree {

    @ApiModelProperty(value = "节点id", example = "1")
    private Long id;


    @ApiModelProperty(value = "节点名称", example = "1")
    public String nodeName;

    @ApiModelProperty(value = "节点类型", example = "1")
    public String type;


    @ApiModelProperty(value = "子节点")
    public List<NodeTree> node=new ArrayList<NodeTree>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<NodeTree> getNode() {
        return node;
    }

    public void setNode(List<NodeTree> node) {
        this.node = node;
    }
}
