package springboot.domain.common;

public class PackListBean {
    /**返回的记录数*/
    private Long count;
    /**返回的数据*/
    private Object list;
    
    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }
    public Object getList() {
        return list;
    }
    public void setList(Object list) {
        this.list = list;
    }
}
