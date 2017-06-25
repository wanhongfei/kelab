package org.kelab.test.util;

import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.kelab.util.JsonWebTokenUtil;

// HS256: HMAC using SHA-256
// HS384: HMAC using SHA-384
// HS512: HMAC using SHA-512
// RS256: RSASSA-PKCS-v1_5 using SHA-256
// RS384: RSASSA-PKCS-v1_5 using SHA-384
// RS512: RSASSA-PKCS-v1_5 using SHA-512
// PS256: RSASSA-PSS using SHA-256 and MGF1 with SHA-256
// PS384: RSASSA-PSS using SHA-384 and MGF1 with SHA-384
// PS512: RSASSA-PSS using SHA-512 and MGF1 with SHA-512
// ES256: ECDSA using P-256 and SHA-256
// ES384: ECDSA using P-384 and SHA-384
// ES512: ECDSA using P-512 and SHA-512

/**
 * @author wwhhf
 * @comment JWT: 1.头部 2.载荷 3.签名
 * @since 2016年6月2日
 */
public class JsonWebTokensTest {

//    	@Test
//    	public void buildDFATree() throws NoSuchAlgorithmException, UnsupportedEncodingException {
//    		Map<String, Object> map=new HashMap();
//    		map.put("hahah", 1);
//    		map.put("username", "wwhhff11");
//    		String s = JsonWebTokenUtil.tokens("whf", map, null, null);
//    		System.out.println(s);
//    		Claims claim=JsonWebTokenUtil.getClaims(s);
//    		System.out.println(claim.get("username"));
//    		System.out.println(EncryptUtil.MD5("util"));
//    //		Jwts.parser().setSigningKey(key).parseClaimsJws(s)
//    	}

	@Test
	public void testEXP() throws Exception {
		Claims claims = JsonWebTokenUtil.token2Claims(
				"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIiLCJyb2xlaWQiOjQsImlzcyI6IlN3dXN0T0pfSldUIiwiZXhwIjoxNDkwMzYzMTE1LCJ1c2VyaWQiOjQzNTgsImlhdCI6MTQ5MDM2MzA1NSwidXNlcm5hbWUiOiJ3d2hoZmYxMSJ9.v1WJg9pDq1DW53pqxLPPW_rbkYHVv_EeChyFIRtmUrg"
		);
		System.out.println(claims);
		/*String urlPattern = "[/localhost[:\\d+]*|/api]+(/.+\\.do)";
		Pattern URL_PATTERN = Pattern.compile(urlPattern);
		String url="http://localhost/api/problem/selectProblem.do";
		Matcher matcher=URL_PATTERN.matcher(url);
		matcher.find();
		System.out.println(matcher.group(1));*/
	}

}
