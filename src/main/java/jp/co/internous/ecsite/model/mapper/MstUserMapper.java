package jp.co.internous.ecsite.model.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import jp.co.internous.ecsite.model.domain.MstUser;
import jp.co.internous.ecsite.model.form.LoginForm;
import jp.co.internous.ecsite.model.form.RegistrerForm;

@Mapper
public interface MstUserMapper {

	@Select(value="select * from mst_user where user_name = #{userName} and password = #{password}")
	MstUser findByUserNameAndPassword(LoginForm form);
	
	@Insert("insert into mst_user (user_name, password, full_name) values (#{userName}, #{password}, #{fullName})")
	@Options(useGeneratedKeys=true, keyProperty="id")
	int insert(MstUser user);
	
	@Select(value="select * from mst_user where user_name = #{userName}")
	MstUser findByUserName(RegistrerForm form);
}
