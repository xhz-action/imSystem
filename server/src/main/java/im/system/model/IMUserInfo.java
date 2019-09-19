package im.system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.mina.core.session.IoSession;
import org.omg.CORBA.IDLType;

import java.io.Serializable;

@Data
@TableName(value = "im_user")
public class IMUserInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "user_no")
    private String userNo;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "nick")
    private String nick;

    @TableField(value = "head_img")
    private String headImg;

    @TableField(exist = false)
    private IoSession ioSession;
}
