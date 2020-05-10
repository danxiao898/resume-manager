package com.breez.security.config;

import com.breez.security.authentication.code.ImageCodeValidateFilter;
import com.breez.security.authentication.mobile.MobileAuthenticationConfig;
import com.breez.security.authentication.mobile.MobileValidateFilter;
import com.breez.security.authorize.CustomAuthorizeConfigurerManager;
import com.breez.security.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.sql.DataSource;

/**
 * alt+/ 导包
 * ctrl+o 覆盖
 */
@Configuration
@EnableWebSecurity//开启Security过滤器链
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启注解方法级别权限控制
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    Logger logger = LoggerFactory.getLogger(getClass());



    //设置默认的加密方式
    @Bean
    public PasswordEncoder passwordEncoder() {
        //明文+随机盐值加密存储
        return new BCryptPasswordEncoder();
    }

    @Autowired
    UserDetailsService customUserDetailsService;
    /**
     * 认证管理器
     * 1. 认证信息（用户名、密码）
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //存储的密码必须是加密的，不然会报错"There is no PasswordEncoder mapped for the id "null""
//        String password  = passwordEncoder().encode("12345678");
//        logger.info("加密之后存储的密码" + password);
//        auth.inMemoryAuthentication().withUser("mengxuegu")
//                .password(password).authorities("ADMIN");

        auth.userDetailsService(customUserDetailsService);

    }


//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//    }

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Autowired
    private ImageCodeValidateFilter imageCodeValidateFilter;


    @Autowired
    DataSource dataSource;

    //校验手机验证码
    @Autowired
    private MobileValidateFilter mobileValidateFilter;

    //手机号认证
    @Autowired
    private MobileAuthenticationConfig mobileAuthenticationConfig;


    @Autowired
    InvalidSessionStrategy invalidSessionStrategy;

    /**
     * 当同一个用户Session数量超过指定值之后会调用这个类
     */
    @Autowired
    SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private CustomAuthorizeConfigurerManager customAuthorizeConfigurerManager;

    /**
     * 记住我功能
     * @return
     */
    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();

        jdbcTokenRepository.setDataSource(dataSource);

        //是否启动项目时自动创建表
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }



    /**
     * 资源权限控制
     * 1. 被拦截的资源
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        logger.info("config:" + securityProperties);
//        http.httpBasic()//采用httpBasic认证方式
        http.addFilterBefore(mobileValidateFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()//采用表单登陆方式
                .loginPage(securityProperties.getAuthentication().getLoginPage())
                .loginProcessingUrl(securityProperties.getAuthentication().getLoginProcessingUrl())//登陆表单提交处理url，默认是/login
                .usernameParameter(securityProperties.getAuthentication().getUsernameParameter())//默认是username
                .passwordParameter(securityProperties.getAuthentication().getPasswordParameter())//默认是password
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
//                .and()
//                .authorizeRequests()//认证请求
//                .antMatchers(securityProperties.getAuthentication().getLoginPage(),
//                        securityProperties.getAuthentication().getImageCodeUrl(),
//                        securityProperties.getAuthentication().getMobilePage(),
//                        securityProperties.getAuthentication().getMobileCodeUrl()
//                        ).permitAll()//放行请求
//
//
//
////                .antMatchers("/user").hasRole("ADMIN")//設置橘色時會自動加上ROLE_作為前綴
//                //有sys:user權限的用戶可以以任意請求方式訪問/user
//                .antMatchers("/user").hasAuthority("sys:user")
//                //有sys:role權限的用戶可以以get方式訪問/role
//                .antMatchers(HttpMethod.GET,"/role").hasAuthority("sys:role")
//                //有sys:permission權限的用戶或者角色"ADMIN"、"ROOT"可以訪問/permission
//                .antMatchers(HttpMethod.GET, "/permission")
//                .access("hasAnyAuthority('sys:permission') or hasAnyRole('ADMIN', 'ROOT')")
//
//
//                .anyRequest().authenticated()//所有访问该应用的http请求都要通过身份认证才能实现访问


                .and()
                .rememberMe()//记住功能配置
                .tokenRepository(jdbcTokenRepository())//保存登录信息
                .tokenValiditySeconds(securityProperties.getAuthentication().getTokenValiditySeconds())//记住我有效时常一周
                .and()
                .sessionManagement()
                .invalidSessionStrategy(invalidSessionStrategy)//处理Session失效的处理类
                .maximumSessions(1)//每个用户在系统中最多有多少个session
                .expiredSessionStrategy(sessionInformationExpiredStrategy)//超过Session最大数执行
//                .maxSessionsPreventsLogin(true)// 当一个用户达到最大的session数，则不允许登录了
                .and().and().csrf().disable()
                .logout().logoutUrl("/user/logout") //设置退出的uti
                .logoutSuccessUrl("/login/page") //配置退出成功后跳转的地址
                .deleteCookies("MY_JSESSIONID")

        ;

        //将所有的授权配置统一的管理起来
        customAuthorizeConfigurerManager.configure(http.authorizeRequests());
        //将手机认证添加到过滤器链上
        http.apply(mobileAuthenticationConfig);
    }

    /**
     * 一般针对静态资源放行
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(securityProperties.getAuthentication().getStaticPath());
    }
}
