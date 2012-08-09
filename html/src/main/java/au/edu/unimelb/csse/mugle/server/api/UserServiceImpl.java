package au.edu.unimelb.csse.mugle.server.api;

import au.edu.unimelb.csse.mugle.client.api.UserService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {

	@Override
	public String getUserNickName() {
		return "default nickname";
	}

	@Override
	public String getUserID() {
		return "default_userid";
	}

}
