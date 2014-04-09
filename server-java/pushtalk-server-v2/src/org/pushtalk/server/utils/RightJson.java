package org.pushtalk.server.utils;

public class RightJson
{
    int msg_type;
    Object msg_content;

    public RightJson(int msg_type, Object msg_content)
    {
        super();
        this.msg_type = msg_type;
        this.msg_content = msg_content;
    }

    public int getMsg_type()
    {
        return msg_type;
    }

    public void setMsg_type(int msg_type)
    {
        this.msg_type = msg_type;
    }

    public Object getMsg_content()
    {
        return msg_content;
    }

    public void setMsg_content(Object msg_content)
    {
        this.msg_content = msg_content;
    }
}
