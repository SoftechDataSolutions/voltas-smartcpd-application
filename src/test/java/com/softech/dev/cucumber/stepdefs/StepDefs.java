package com.softech.dev.cucumber.stepdefs;

import com.softech.dev.WebinarappApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = WebinarappApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
