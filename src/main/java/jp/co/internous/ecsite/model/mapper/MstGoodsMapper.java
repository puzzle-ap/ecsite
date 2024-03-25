package jp.co.internous.ecsite.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import jp.co.internous.ecsite.model.domain.MstGoods;

@Mapper
public interface MstGoodsMapper {
	
	@Select(value="select * from mst_goods")
	List<MstGoods> findAll();
	
	@Insert("insert into mst_goods (goods_name, price) values (#{goodsName}, #{price})")
	@Options(useGeneratedKeys=true, keyProperty="id")
	int insert(MstGoods goods);

	@Update("delete from mst_goods where id = #{id}")
	int deleteById(int id);
	
}
