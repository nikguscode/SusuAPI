/**
 * <p>This package is designed to separate authentication logic from other request types, making the codebase more modular.</p>
 *
 * <p>
 * The interface {@link com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication.AuthenticationRequestSender
 * AuthenticationRequestSender} provides an abstraction for sending authentication requests. This interface enables different
 * implementations without tightly coupling them to controllers, promoting flexibility and decoupling.
 * </p>
 *
 * <p>
 * The abstract class {@link com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication.AuthenticationRequest
 * AuthenticationRequest} centralizes common code for various authentication mechanisms. It prevents code duplication across
 * different authentication implementations. This class also defines an abstract method
 * {@link com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication.AuthenticationRequest#setParameters(com.nikguscode.SusuAPI.dto.iternal.StudentDto, com.nikguscode.SusuAPI.model.entities.configuration.Authentication, java.net.http.HttpClient)
 * setParameters()}, which allows each authentication class to set its own specific request parameters.
 * </p>
 *
 * <p>
 * {@link com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication.StudlkAuthenticationRequest
 * StudlkAuthenticationRequest} implements authentication for the studlk.susu.ru platform.
 * </p>
 *
 * <p>
 * {@link com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication.OnlineAuthenticationRequest
 * OnlineAuthenticationRequest} provides authentication for the online.susu.ru platform.
 * </p>
 *
 * <p>
 * {@link com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication.EduAuthenticationRequest
 * EduAuthenticationRequest} is responsible for authentication for the edu.susu.ru platform.
 * </p>
 */

package com.nikguscode.SusuAPI.model.service.querymanager.requests.authentication;