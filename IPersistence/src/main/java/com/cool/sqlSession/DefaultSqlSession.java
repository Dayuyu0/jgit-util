package com.cool.sqlSession;

import com.cool.pojo.Configuration;
import com.cool.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }


    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        // 使用JDK动态代理来为Dao接口生成代理对象，并返回

        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 底层都还是去执行JDBC代码 //根据不同情况，来调用selctList或者selectOne
                // 准备参数 1：statmentid :sql语句的唯一标识：namespace.id= 接口全限定名.方法名
                // 方法名：findAll
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();

                String statementId = className+"."+methodName;
                //执行增删改操作

                // 准备参数2：params:args
                // 获取被调用方法的返回值类型
                Type genericReturnType = method.getGenericReturnType();
                // 判断是否进行了 泛型类型参数化
                if (methodName == "findAll"){
                    List<Object> objects = selectList(statementId, args);
                    return objects;
                }else if (methodName == "findByCondition"){
                    return selectOne(statementId, args);
                }else if (methodName == "insert"){
                    insert(statementId, args);
                    return null;
                }else if (methodName == "deleteById"){
                    deleteById(statementId, args);
                    return null;
                }else if (methodName == "updateById") {
                    updateById(statementId, args);
                    return null;
                }
                return null;
            }


        });

        return (T) proxyInstance;
    }

    @Override
    public <E> List<E> selectList(String statementid, Object... params) throws Exception {

        //将要去完成对simpleExecutor里的query方法的调用
        simpleExecutor simpleExecutor = new simpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementid);
        List<Object> list = simpleExecutor.query(configuration, mappedStatement, params);

        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementid, Object... params) throws Exception {
        //查询所有方法
        List<Object> objects = selectList(statementid, params);
        if(objects.size()==1){
            return (T) objects.get(0);
        }else {
            throw new RuntimeException("查询结果为空或者返回结果过多");
        }


    }

    @Override
    public void insert(String statementid, Object... params) throws Exception {
        //对simpleExecutor的query方法调用
        simpleExecutor simpleExecutor = new simpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementid);
        simpleExecutor.query(configuration, mappedStatement, params);
    }

    @Override
    public void updateById(String statementid, Object... params) throws Exception {
        //对simpleExecutor的query方法调用
        simpleExecutor simpleExecutor = new simpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementid);
        simpleExecutor.query(configuration, mappedStatement, params);
    }

    @Override
    public void deleteById(String statementid, Object... params) throws Exception {
        //对simpleExecutor的query方法调用
        simpleExecutor simpleExecutor = new simpleExecutor();
        MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementid);
        simpleExecutor.query(configuration, mappedStatement, params);
    }




}
