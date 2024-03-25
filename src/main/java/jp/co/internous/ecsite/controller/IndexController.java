package jp.co.internous.ecsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.ecsite.model.domain.MstGoods;
import jp.co.internous.ecsite.model.domain.MstUser;
import jp.co.internous.ecsite.model.dto.HistoryDto;
import jp.co.internous.ecsite.model.form.CartForm;
import jp.co.internous.ecsite.model.form.HistoryForm;
import jp.co.internous.ecsite.model.form.LoginForm;
import jp.co.internous.ecsite.model.form.RegistrerForm;
import jp.co.internous.ecsite.model.mapper.MstGoodsMapper;
import jp.co.internous.ecsite.model.mapper.MstUserMapper;
import jp.co.internous.ecsite.model.mapper.TblPurchaseMapper;

@Controller
@RequestMapping("/ecsite")
public class IndexController {

	@Autowired
	private MstGoodsMapper goodsMapper;

	@Autowired
	private MstUserMapper userMapper;

	@Autowired
	private TblPurchaseMapper purchaseMapper;

	private Gson gson = new Gson();

	@GetMapping("/")
	public String index(Model model, LoginForm f) {
		List<MstGoods> goods = goodsMapper.findAll();
		model.addAttribute("goods", goods);

		return "index";
	}

	@GetMapping("/signup")
	public String signup() {

		return "signup";
	}

	@PostMapping("/signup/register")
	public String signupRegister(RegistrerForm f, Model m) {
		MstUser user = new MstUser();
		MstUser userCheck = userMapper.findByUserName(f);
		
		m.addAttribute("fullName", f.getFullName());
		m.addAttribute("userName", f.getUserName());
		m.addAttribute("password", f.getPassword());

		if (f.getFullName().isEmpty() || f.getUserName().isEmpty() || f.getPassword().isEmpty()) {
			m.addAttribute("errorMessage", "全ての項目を入力してください。");
			return "signup";
		}

		if (userCheck != null) {
			m.addAttribute("errorMessage", "このユーザー名はすでに使用されています。");
			return "signup";
		}

		user.setFullName(f.getFullName());
		user.setUserName(f.getUserName());
		user.setPassword(f.getPassword());

		userMapper.insert(user);

		return "redirect:/ecsite/";
	}

	@ResponseBody
	@PostMapping("/api/login")
	public String loginApi(@RequestBody LoginForm f) {
		MstUser user = userMapper.findByUserNameAndPassword(f);

		if (user == null) {
			user = new MstUser();
			user.setFullName("ゲスト");
		}

		return gson.toJson(user);
	}

	@ResponseBody
	@PostMapping("/api/purchase")
	public int purchaseApi(@RequestBody CartForm f) {

		f.getCartList().forEach((c) -> {
			int total = c.getPrice() * c.getCount();
			purchaseMapper.insert(f.getUserId(), c.getId(), c.getGoodsName(), c.getCount(), total);
		});

		return f.getCartList().size();
	}

	@ResponseBody
	@PostMapping("/api/history")
	public String historyApi(@RequestBody HistoryForm f) {
		int userId = f.getUserId();
		List<HistoryDto> history = purchaseMapper.findHistory(userId);

		return gson.toJson(history);
	}
}
