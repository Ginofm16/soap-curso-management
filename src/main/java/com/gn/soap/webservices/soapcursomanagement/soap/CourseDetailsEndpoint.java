package com.gn.soap.webservices.soapcursomanagement.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.gn.courses.CourseDetails;
import com.gn.courses.DeleteCourseDetailsRequest;
import com.gn.courses.DeleteCourseDetailsResponse;
import com.gn.courses.GetAllCourseDetailsRequest;
import com.gn.courses.GetAllCourseDetailsResponse;
import com.gn.courses.GetCourseDetailsRequest;
import com.gn.courses.GetCourseDetailsResponse;
import com.gn.soap.webservices.soapcursomanagement.soap.bean.Course;
import com.gn.soap.webservices.soapcursomanagement.soap.exception.CourseNotFoundException;
import com.gn.soap.webservices.soapcursomanagement.soap.service.CourseDetailsService;
import com.gn.soap.webservices.soapcursomanagement.soap.service.CourseDetailsService.Status;

@Endpoint
public class CourseDetailsEndpoint {
	
	/*Endpoint is the component that receives the request, initiates the processing and sends 
	 * the response back.*/
	
	@Autowired
	CourseDetailsService service;

	//methos
	//input - GetCourseDetailsRequest
	//output - GetCourseDetailsResponse
	
	/*si viene una solicitus con el namespace http://....., y el nombre GetCourseDetailsRequest
	 * entonces debe llamar al metdo processCourseDetailsRequest, lo hacemos mediante la anotacion Payload*/
	//http://gn.com/courses
	//GetCourseDetailsRequest
	@PayloadRoot(namespace="http://gn.com/courses", localPart = "GetCourseDetailsRequest")
	/*una vez que se obtenga la respuesta se necesita convertila nuevamente en XML, porque el response
	 * del  return es java*/
	@ResponsePayload
	public GetCourseDetailsResponse processCourseDetailsRequest(@RequestPayload GetCourseDetailsRequest request) {
		
		Course course = service.findById(request.getId());
		
		if(course == null)
			throw new CourseNotFoundException("Invalid Course Id "+ request.getId());
		
		return mapCourseDetails(course);
		
	}

	private GetCourseDetailsResponse mapCourseDetails(Course course) {
		GetCourseDetailsResponse response = new GetCourseDetailsResponse();
		
		response.setCourseDetails(mapCourse(course));
		return response;
	}
	
	private GetAllCourseDetailsResponse mapAllCourseDetails(List<Course> courses) {
		GetAllCourseDetailsResponse response = new GetAllCourseDetailsResponse();
		
		for(Course course : courses) {
			CourseDetails mapCourse = mapCourse(course);
			response.getCourseDetails().add(mapCourse);
		}
		
		return response;
	}

	private CourseDetails mapCourse(Course course) {
		CourseDetails courseDetails = new CourseDetails();
		courseDetails.setId(course.getId());
		courseDetails.setName(course.getName());
		courseDetails.setDescription(course.getDescription());
		return courseDetails;
	}

	@PayloadRoot(namespace="http://gn.com/courses", localPart = "GetAllCourseDetailsRequest")
	@ResponsePayload
	public GetAllCourseDetailsResponse processAllCourseDetailsRequest(@RequestPayload GetAllCourseDetailsRequest request) {
		
		List<Course> courses = service.findAll();
		
		return mapAllCourseDetails(courses);
	}
	
	@PayloadRoot(namespace="http://gn.com/courses", localPart = "DeleteCourseDetailsRequest")
	@ResponsePayload
	public DeleteCourseDetailsResponse deleteCourseDetailsRequest(@RequestPayload DeleteCourseDetailsRequest request) {
		
		Status status = service.deleteById(request.getId());
		
		DeleteCourseDetailsResponse response = new DeleteCourseDetailsResponse();
		response.setStatus(mapStatus(status));
		return response;
	}

	private com.gn.courses.Status mapStatus(Status status) {
		if(status == Status.FAILURE)
			return com.gn.courses.Status.FAILURE;
		return com.gn.courses.Status.SUCCESS;
	}
	
}
