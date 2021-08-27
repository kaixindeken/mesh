package com.tanklab.mathless.pojo;

import lombok.Data;

@Data
public class Message {

		private boolean success;
		private String info;
		private Object data;

		public Message(){

		}

		public Message(boolean success, String info){
				this.success = success;
				this.info = info;
		}

		public Message(boolean success, String info, Object data){
				this.success = success;
				this.info = info;
				this.data = data;
		}

}
