/**
 * 员工表
 * @TableName employee
 */
public class Employee {
    /**
     * 主键
     */
    private Long eid;

    /**
     * 工号
     */
    private String eno;

    /**
     * 姓名
     */
    private String ename;

    /**
     * 年龄
     */
    private Integer eage;

    /**
     * 性别
     */
    private String egender;

    /**
     * 工种
     */
    private String ejob;

    /**
     * 入职时间
     */
    private Date eentrydate;

    /**
     * 基本薪资
     */
    private BigDecimal esalary;

    /**
     * 在职状态(1.在职 2.离职)
     */
    private Integer estate;

    /**
     * 部门编号
     */
    private Integer did;
}