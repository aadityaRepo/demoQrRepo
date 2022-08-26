package com.pixeltrice.springbootQRcodegeneratorapp;


import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.json.JSONObject;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class QRCodeController {
	
	private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";
	private final int WIDTH = 300;
	private final int HEIGHT = 300;
	private final String QR_TEXT = "Spring Boot REST API to generate QR Code - Websparrow.org";
	private final UserRepository userRepository;
	public QRCodeController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


    @GetMapping(value = "/genrateAndDownloadQRCode/{codeText}/{width}/{height}")
		public void download(
				@PathVariable("codeText") String codeText,
				@PathVariable("width") Integer width,
				@PathVariable("height") Integer height)
			    throws Exception {
			        QRCodeGenerator.generateQRCodeImage(codeText, width, height, QR_CODE_IMAGE_PATH);
			    }



    @RequestMapping(value = "/genrateQRCode" ,method=RequestMethod.POST)
   	public ResponseEntity<byte[]> generateQRCode(@ModelAttribute  Developer developer)
   		    throws Exception {
   		    	   	if(developer.isWifi()){
	return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(QRCodeGenerator.getQRCodeImage(generateJsonWithWifi( developer), WIDTH, HEIGHT));
					} else {
	return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(QRCodeGenerator.getQRCodeImage(generateJson( developer), WIDTH, HEIGHT));
	     }
    }

	@GetMapping("/greeting")
	public ModelAndView greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		ModelAndView mav = new ModelAndView("developer");
		Developer dev = new Developer();
		mav.addObject("developer", dev);
		return mav;
	}

    String generateJson(Developer developer) throws JSONException {
		JSONObject jsonObjstr= new JSONObject();
		jsonObjstr.put("android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME", StringUtils.trim(developer.getFirstName()));
		jsonObjstr.put("android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION",StringUtils.trim(developer.getLastName()));
		jsonObjstr.put("android.app.extra.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM",StringUtils.trim(developer.getEmail()));
        return jsonObjstr.toString();
	}

	String generateJsonWithWifi(Developer developer) throws JSONException {
		JSONObject jsonObjstr= new JSONObject();
		jsonObjstr.put("android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME", StringUtils.trim(developer.getFirstName()));
		jsonObjstr.put("android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION",StringUtils.trim(developer.getLastName()));
		jsonObjstr.put("android.app.extra.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM",StringUtils.trim(developer.getEmail()));
		jsonObjstr.put("android.app.extra.PROVISIONING_WIFI_SSID", StringUtils.trim(developer.getWifissid()));
		jsonObjstr.put("android.app.extra.PROVISIONING_WIFI_PASSWORD",StringUtils.trim(developer.getWifipassword()));
		jsonObjstr.put("android.app.extra.PROVISIONING_WIFI_SECURITY_TYPE",StringUtils.trim("WPA"));

		return jsonObjstr.toString();
	}

	@GetMapping("/users")
	public List<User> getUsers() {
		return (List<User>) userRepository.findAll();
	}

	@PostMapping("/users")
	void addUser(@RequestBody User user) {
		System.out.println(user.getName());
		userRepository.save(user);
	}

	@PostMapping("/deleteusr")
	void DeleteUser(@RequestBody String id) {
		System.out.println("Delete from the row user Id"+ String.valueOf(id));

	}


	@GetMapping("/kafka")
	public String getKafkaProducer() {
				// create instance for properties to access producer configs
				Properties props = new Properties();
				//Assign localhost id
		         props.put("bootstrap.servers", "localhost:9092");

				//Set acknowledgements for producer requests.
				props.put("acks", "all");

				//If the request fails, the producer can automatically retry,
				props.put("retries", 0);

				//Specify buffer size in config
				props.put("batch.size", 16384);

				//Reduce the no of requests less than 0
				props.put("linger.ms", 1);

				//The buffer.memory controls the total amount of memory available to the producer for buffering.
				props.put("buffer.memory", 33554432);

				props.put("key.serializer",
						"org.apache.kafka.common.serialization.StringSerializer");

				props.put("value.serializer",
						"org.apache.kafka.common.serialization.StringSerializer");

				Producer<String, String> producer = new KafkaProducer
						<String, String>(props);

		       producer.send(new ProducerRecord<String, String>("quickstart-events","Key", "This is my Value"));

				/*for(int i = 0; i < 10; i++)
					producer.send(new ProducerRecord<String, String>("quickstart-events",
							Integer.toString(i), Integer.toString(i)));*/
				System.out.println("Message sent successfully");
				producer.close();

		return "Message sent successfully";
	}





}
