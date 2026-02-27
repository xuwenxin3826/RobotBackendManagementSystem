package com.ruoyi.common.exception.task;


import com.ruoyi.common.enums.ReturnNo;
import com.ruoyi.common.exception.base.BaseException;

/**
 * 机器人任务管理异常类
 *
 * @author ruoyi
 */
public class TaskmgtException extends BaseException
{
    private static final long serialVersionUID = 1L;

    private ReturnNo errno;

    public TaskmgtException(ReturnNo errno, Object[] args, String msg) {
        super("taskmgt", String.valueOf(errno.getErrNo()), args, msg);
        this.errno = errno;
    }

    public ReturnNo getErrno(){
        return this.errno;
    }
}