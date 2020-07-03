package com.ucas.paper.controller.admin;

import com.ucas.paper.PaperApplication;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PaperApplication.class})
@AutoConfigureMockMvc //测试接口用
public class AdminControllerTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Before
    public void testBefore(){
        log.info("测试前");
    }
    @After
    public void testAfter(){
        log.info("测试后");
    }
    @Autowired
    private MockMvc mockMvc;
    /**
     * 测试 /mockTest
     *
     *
     */
    @Test
    public void mockTest()throws Exception{
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/index")).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        int status=mvcResult.getResponse().getStatus();
        //打印出状态码，200就是成功
        log.info("状态码="+status);
        Assert.assertEquals(200,status);
    }

    @Test
    public void loginPage() {
    }

    @Test
    public void dashboard() {
    }

    @Test
    public void login() {
    }

    @Test
    public void logout() {
    }

    @Test
    public void adminEdit() {
    }

    @Test
    public void adminEditPassword() {
    }

    @Test
    public void aboutusList() {
    }

    @Test
    public void aboutusEdit() {
    }

    @Test
    public void purposeEdit() {
    }
}