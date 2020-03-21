package com.Hello.Controller;


import com.Hello.dto.AccessTokenDto;
import com.Hello.dto.GithubUser;
import com.Hello.mapper.UserMapper;
import com.Hello.model.UserModel;
import com.Hello.provider.GithubProvider;
import com.Hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserService userService;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                            HttpServletResponse response){
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setState(state);
        accessTokenDto.setRedirect_uri(redirectUri);

        // 拿到access_token
        String accessToken = githubProvider.getAccessToken(accessTokenDto);
        // 拿到user
        GithubUser githubUser = githubProvider.getGitUser(accessToken);

        if (githubUser != null){

            UserModel userModel = new UserModel();
            String token = UUID.randomUUID().toString();
            userModel.setToken(token);
            userModel.setName(githubUser.getName());
            userModel.setAccountId(String.valueOf(githubUser.getId()));
            userModel.setAvatarUrl(githubUser.getAvatar_url());
            userModel.setBio(githubUser.getBio());
            userService.createOrUpdate(userModel);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else {
            // 登陆失败，重新登陆
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                          HttpServletResponse response){
        // 删除session
        request.getSession().removeAttribute("user");
        // 删除cookie
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
