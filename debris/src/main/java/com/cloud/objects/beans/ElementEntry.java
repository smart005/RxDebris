package com.cloud.objects.beans;

import android.text.TextUtils;

import com.cloud.objects.ObjectJudge;
import com.cloud.objects.utils.JsonUtils;
import com.cloud.objects.utils.PathsUtils;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/4/4
 * Description:element节点数据
 * Modifier:
 * ModifyContent:
 */
public class ElementEntry {

    //本级元素列表
    private LinkedList<String> elements = new LinkedList<String>();
    //根元素列表
    private ElementEntry rootElements;
    //上级元素列表
    private ElementEntry prevElements;
    //下级元素列表
    private TreeMap<String, ElementEntry> nextElements = new TreeMap<String, ElementEntry>();
    //路径
    private String path = "/";

    /**
     * 添加元素
     *
     * @param element 元数据
     * @return ElementEntry
     */
    public synchronized ElementEntry addElement(String element) {
        if (TextUtils.isEmpty(element) || elements.contains(element)) {
            return this;
        }
        //root element
        if (this.prevElements == null) {
            if (this.rootElements == null) {
                this.rootElements = this;
            }
        } else if (this.rootElements == null) {
            bindRootNode(this.prevElements);
        }
        //添加元素
        elements.addLast(element);
        return this;
    }

    /**
     * 添加下一节点数据
     *
     * @param element 元数据
     * @return ElementEntry
     */
    public ElementEntry next(String element) {
        if (TextUtils.isEmpty(element)) {
            //元素空返回当前对象
            return this;
        }
        if (this.prevElements == null) {
            if (this.rootElements == null) {
                this.rootElements = this;
            }
        } else if (this.rootElements == null) {
            bindRootNode(this.prevElements);
        }
        //当前节点
        String mkey;
        if (ObjectJudge.isNullOrEmpty(this.elements)) {
            mkey = this.path;
        } else {
            String last = this.elements.getLast();
            mkey = PathsUtils.combine(this.path, last);
        }
        ElementEntry matchEntry = getMatchEntry(getRootElements(), mkey);
        if (matchEntry.prevElements == null) {
            matchEntry.prevElements = this;
        }
        matchEntry.path = mkey;
        matchEntry.addElement(element);
        return matchEntry;
    }

    private ElementEntry getMatchEntry(ElementEntry entry, String key) {
        ElementEntry childEntry = getMatchChildEntry(entry, key);
        if (childEntry != null) {
            return childEntry;
        }
        ElementEntry elementEntry = new ElementEntry();
        elementEntry.prevElements = this;
        this.nextElements.put(key, elementEntry);
        return elementEntry;
    }

    private ElementEntry getMatchChildEntry(ElementEntry entry, String key) {
        if (TextUtils.equals(entry.path, key)) {
            return entry;
        }
        if (entry.nextElements.containsKey(key)) {
            return entry.nextElements.get(key);
        }
        ElementEntry elementEntry;
        for (Map.Entry<String, ElementEntry> entryEntry : entry.nextElements.entrySet()) {
            elementEntry = getMatchChildEntry(entryEntry.getValue(), key);
            if (elementEntry != null) {
                return elementEntry;
            }
        }
        //没有完全匹配项则获取与key层级相同的元数据项
        String[] split = key.split("/");
        elementEntry = getLastElementMatchItem(getRootElements(), key, split[split.length - 1]);
        return elementEntry;
    }

    private ElementEntry getLastElementMatchItem(ElementEntry entry, String key, String lastElement) {
        if (entry.elements.contains(lastElement)) {
            if (entry.nextElements.containsKey(key)) {
                return entry.nextElements.get(key);
            }
            ElementEntry elementEntry = new ElementEntry();
            elementEntry.path = key;
            entry.nextElements.put(key, elementEntry);
            return elementEntry;
        }
        for (Map.Entry<String, ElementEntry> entryEntry : entry.nextElements.entrySet()) {
            ElementEntry sameLevelElement = getLastElementMatchItem(entryEntry.getValue(), key, lastElement);
            if (sameLevelElement != null) {
                return sameLevelElement;
            }
        }
        return null;
    }

    /**
     * 添加上一节点数据
     *
     * @param prevSeriesCount 返回上层级数
     * @param element         元数据
     * @return ElementEntry
     */
    public ElementEntry prev(int prevSeriesCount, String element) {
        //上一节点
        ElementEntry elements = getPrevElements(prevSeriesCount);
        if (elements.prevElements == null) {
            //如果上一节点为空那么当前对象即是根节点对象
            if (elements.rootElements == null) {
                elements.rootElements = this;
            }
        } else if (elements.rootElements == null) {
            //root element
            bindRootNode(elements);
        }
        elements.addElement(element);
        return elements;
    }

    private ElementEntry getPrevElements(int prevSeriesCount) {
        ElementEntry entry = this.prevElements;
        //由于首次直接调用prev则this.prevElements==null,因此entry=this;
        if (entry == null) {
            entry = this;
        }
        if (prevSeriesCount <= 1) {
            return entry;
        }
        for (int i = 1; i < prevSeriesCount; i++) {
            if (entry.prevElements == null) {
                entry = this;
                break;
            } else {
                entry = entry.prevElements;
            }
        }
        return entry;
    }

    private void bindRootNode(ElementEntry elementEntry) {
        if (elementEntry == null) {
            return;
        }
        if (elementEntry.prevElements == null) {
            this.rootElements = elementEntry;
        } else {
            bindRootNode(elementEntry.prevElements);
        }
    }

    /**
     * 添加上一节点数据
     *
     * @param element 元数据
     * @return ElementEntry
     */
    public ElementEntry prev(String element) {
        return prev(1, element);
    }

    /**
     * 获取根元素对象
     *
     * @return ElementEntry
     */
    public ElementEntry getRootElements() {
        if (this.rootElements == null) {
            return this;
        } else {
            return this.rootElements;
        }
    }

    /**
     * 获取所有元素路径
     *
     * @return 元素路径集合
     */
    public LinkedList<String> getAllElementPaths() {
        LinkedList<String> list = new LinkedList<String>();
        ElementEntry rootElements = getRootElements();
        for (String element : rootElements.elements) {
            list.addLast(PathsUtils.combine(rootElements.path, element));
        }
        getElementPaths(list, rootElements.nextElements);
        return list;
    }

    private void getElementPaths(LinkedList<String> list, TreeMap<String, ElementEntry> nextElements) {
        if (ObjectJudge.isNullOrEmpty(nextElements)) {
            return;
        }
        for (Map.Entry<String, ElementEntry> entry : nextElements.entrySet()) {
            ElementEntry value = entry.getValue();
            for (String element : value.elements) {
                list.addLast(PathsUtils.combine(entry.getKey(), element));
            }
            if (!ObjectJudge.isNullOrEmpty(value.nextElements)) {
                getElementPaths(list, value.nextElements);
            }
        }
    }

    private class EleItem {
        public String element;
    }

    @Override
    public String toString() {
        LinkedList<EleItem> items = new LinkedList<EleItem>();
        ElementEntry rootElements = getRootElements();
        getChildElements(items, rootElements);
        return JsonUtils.toJson(items);
    }

    private void getChildElements(LinkedList<EleItem> items, ElementEntry entry) {
        for (String element : entry.elements) {
            EleItem eleItem = new EleItem();
            eleItem.element = element;
            items.add(eleItem);
        }
        if (!ObjectJudge.isNullOrEmpty(entry.nextElements)) {
            for (Map.Entry<String, ElementEntry> entryEntry : entry.nextElements.entrySet()) {
                getChildElements(items, entryEntry.getValue());
            }
        }
    }
}
