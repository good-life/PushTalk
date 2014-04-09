package org.pushtalk.server.utils;

public class ErrorJson
{
    int error_code;
    String error_msg;

    public ErrorJson(int error_code, String error_msg)
    {
        super();
        this.error_code = error_code;
        this.error_msg = error_msg;
    }

    public int getError_code()
    {
        return error_code;
    }

    public void setError_code(int error_code)
    {
        this.error_code = error_code;
    }

    public String getError_msg()
    {
        return error_msg;
    }

    public void setError_msg(String error_msg)
    {
        this.error_msg = error_msg;
    }
}
