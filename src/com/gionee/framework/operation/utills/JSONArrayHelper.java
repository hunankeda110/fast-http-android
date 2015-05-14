package com.gionee.framework.operation.utills;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.gionee.framework.model.bean.HttpConstants;

/**
 * com.gionee.app_framework.model.bean.JSONArrayHelper
 * 
 * @author yuwei <br/>
 *         create at 2013-3-19 下午1:39:40 TODO
 */
public class JSONArrayHelper {

    private static final String TAG = JSONArrayHelper.class.getSimpleName();
    private List<Object> values;

    /**
     * 
     * @param jsonArray
     */
    @SuppressWarnings("unchecked")
    public JSONArrayHelper(JSONArray jsonArray) {
        if (jsonArray == null) {
            throw new NullPointerException();
        }
        try {
            Field field = null;
            try {
                field = JSONArray.class.getDeclaredField("values");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                try {
                    field = JSONArray.class.getDeclaredField("myArrayList");
                } catch (NoSuchFieldException e1) {
                    e1.printStackTrace();
                    field = JSONArray.class.getDeclaredFields()[0];
                }
            }
            field.setAccessible(true);
            values = (List<Object>) field.get(jsonArray);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object remove(int index) {
        return values.remove(index);
    }

    public Object remove(Object value) {
        return values.remove(value);
    }

    public int length() {
        return values.size();
    }

    public void clear() {
        values.clear();
    }

    public void addToLast(Object jsonObj) {
        if (values.contains(jsonObj))
            return;
        values.add(values.size(), jsonObj);

    }

    public JSONArrayHelper add(int index, JSONObject jo) {
        values.add(index, jo);
        return this;
    }

    public boolean addAll(Collection<? extends Object> copyFrom) {
        return values.addAll(copyFrom);
    }

    public boolean isContainsObject(Object object) {
        if (values == null)
            return false;
        return values.contains(object);

    }

    // 处理数据重复，以及数据不重复但是id相同，即表示同一条数据的情况
    public boolean isContainsObjectStr(Object object, List<Object> copyValues) {
        try {
            String idOnMore = ((JSONObject) object).optString(HttpConstants.Request.ID);
            for (Object obj : copyValues) {
                String idOnOld = ((JSONObject) obj).optString(HttpConstants.Request.ID);
                // 新旧jsonArray 中数据不同，但是id相同，即同一项的数据. 删除旧数据，添加新数据。
                if (idOnMore.equals(idOnOld)) {
                    remove(obj);
                    return false;
                }

                // 新旧jsonArray中数据相同，不重复添加。
                if (obj.toString().equals(object.toString())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 如果新添加的jsonarray项中出现null或""; 不添加.
            return true;
        }
        return false;
    }

    public boolean addToHead(List<Object> copyFrom) {
        copyFrom.addAll(values);
        values = copyFrom;
        return true;
    }

    public boolean addAll(JSONArray more, boolean ahead) {
        if (more != null && more.length() > 0) {
            try {
                Field field = null;
                field = JSONArray.class.getDeclaredField("values");
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                List<Object> moreValues = (List<Object>) field.get(more);
                if (ahead) {
                    addToHead(moreValues);
                } else {
                    // 采用去重复处理。
                    long start = System.currentTimeMillis();
                    LogUtils.logd(TAG, LogUtils.getThreadName() + " start: ");
                    List<Object> copyValues = new ArrayList<Object>(values);
                    for (Object object : moreValues) {
                        if (!isContainsObjectStr(object, copyValues)) {
                            values.add(object);
                        }
                    }
                    long end = System.currentTimeMillis();
                    LogUtils.logd(TAG, LogUtils.getThreadName() + " end, time : " + (end - start));
//                    values.addAll(moreValues);
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static List<JSONObject> toList(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        try {
            Field field = null;
            try {
                field = JSONArray.class.getDeclaredField("values");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                try {
                    field = JSONArray.class.getDeclaredField("myArrayList");
                } catch (NoSuchFieldException e1) {
                    e1.printStackTrace();
                    field = JSONArray.class.getDeclaredFields()[0];
                }
            }
            field.setAccessible(true);
            return (List<JSONObject>) field.get(jsonArray);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取两个List的不同元素
     * 
     * @param list1
     * @param list2
     * @return
     */
    private static List<Object> getDiffrent(List<Object> list1, List<Object> list2) {
        long st = System.nanoTime();
        Map<Object, Integer> map = new HashMap<Object, Integer>(list1.size() + list2.size());
        List<Object> diff = new ArrayList<Object>();
        List<Object> maxList = list1;
        List<Object> minList = list2;
        if (list2.size() > list1.size()) {
            maxList = list2;
            minList = list1;
        }
        for (Object obj : maxList) {
            map.put(obj, 1);
        }
        for (Object obj : minList) {
            Integer cc = map.get(obj);
            if (cc != null) {
                map.put(obj, ++cc);
                continue;
            }
            map.put(obj, 1);
        }
        for (Map.Entry<Object, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
            }
        }
        System.out.println("getDiffrent total times " + (System.nanoTime() - st));
        return diff;
    }
}
