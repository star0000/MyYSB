package com.mds.myysb.bean;

public class UserIdBean {


    /**
     * errNo : 0
     * errMsg :
     * data : {"sessionId":"443b3bfe-1c52-48cc-90b8-7687535b1705","userId":"48a3fed3-8806-46da-8630-bf8183031f6a"}
     */
        /**
         * sessionId : 443b3bfe-1c52-48cc-90b8-7687535b1705
         * userId : 48a3fed3-8806-46da-8630-bf8183031f6a
         */

        private String sessionId;
        private String userId;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

}
