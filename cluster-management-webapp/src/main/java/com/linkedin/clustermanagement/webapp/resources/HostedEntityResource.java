package com.linkedin.clustermanagement.webapp.resources;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import com.linkedin.clustermanager.core.ClusterDataAccessor.ClusterPropertyType;
import com.linkedin.clustermanager.tools.ClusterSetup;

public class HostedEntityResource extends Resource
{
  public HostedEntityResource(Context context,
      Request request,
      Response response) 
  {
    super(context, request, response);
    getVariants().add(new Variant(MediaType.TEXT_PLAIN));
    getVariants().add(new Variant(MediaType.APPLICATION_JSON));
  }

  public boolean allowGet()
  {
    return true;
  }
  
  public boolean allowPost()
  {
    return false;
  }
  
  public boolean allowPut()
  {
    return false;
  }
  
  public boolean allowDelete()
  {
    return false;
  }
  
  public Representation represent(Variant variant)
  {
    StringRepresentation presentation = null;
    try
    {
      String zkServer = (String)getContext().getAttributes().get("zkServer");
      String clusterName = (String)getRequest().getAttributes().get("clusterName");
      String entityId = (String)getRequest().getAttributes().get("entityId");
      presentation = getIdealStateRepresentation(zkServer, clusterName, entityId);
    }
    
    catch(Exception e)
    {
      getResponse().setEntity("ERROR " + e.getMessage(),
          MediaType.TEXT_PLAIN);
      getResponse().setStatus(Status.SUCCESS_OK);
    }  
    return presentation;
  }
  
  StringRepresentation getIdealStateRepresentation(String zkServerAddress, String clusterName, String entityId) throws JsonGenerationException, JsonMappingException, IOException
  {
    String message = "Ideal state for entity " + entityId + " in cluster "+ clusterName + "\n";
    message += ClusterRepresentationUtil.getClusterPropertyAsString(zkServerAddress, clusterName, ClusterPropertyType.IDEALSTATES, entityId, MediaType.APPLICATION_JSON);
    
    StringRepresentation representation = new StringRepresentation(message, MediaType.APPLICATION_JSON);
    
    return representation;
  }
}
