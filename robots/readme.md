自定义类型的异常需要在全局异常处理中添加（GlobalExceptionHandler）
@ExceptionHandler(DeleteNoAllowedException.class)
public AjaxResult deleteNoAllowedException(DeleteNoAllowedException e)
{
log.error(e.getMessage());
return AjaxResult.error(e.getMessage());
}