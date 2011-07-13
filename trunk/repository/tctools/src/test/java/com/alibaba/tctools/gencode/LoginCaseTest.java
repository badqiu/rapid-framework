package com.alibaba.tctools.gencode;

import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

public class LoginCaseTest {

    /**
     * @step 调整IP
     * @uid 1
     * @preCondition username="zhoulai",password="0000"
     * @methodName login
     * @expectResult 通过
     */
    @Test
    public void login() {

        System.out.println("test");
    }

    /**
     * @step step1
     * @uid 2
     * @preCondition username="zhoulai"
     * @methodName loginWithErrorPassword1
     * @expectResult 密码错误
     */
    @Test
    public void loginWithErrorPassword1() {

    }

    /**
     * @step
     * @uid 3
     * @preCondition
     * @methodName add
     * @expectResult
     */
    @Test
    public void add() {

    }

}
