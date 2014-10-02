package com.taskadapter.redmineapi;

import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.WikiPage;
import com.taskadapter.redmineapi.bean.WikiPageDetail;
import com.taskadapter.redmineapi.internal.Transport;
import com.taskadapter.redmineapi.internal.URIConfigurator;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author mario
 */
public class RedmineManagerUnitTest {

    private static final String HOST = "http://localhost";
    private static final String API = "whatever";
    
    private RedmineManager manager;
    private HttpClient client;
    
    @Before
    public void setUp() {
        client = Mockito.mock(HttpClient.class);
        manager =
                new RedmineManager(
                    new Transport(new URIConfigurator(HOST, API), client),
                    RedmineManagerFactory.createDefaultTransportConfig().shutdownListener
                );        
    }
    
    @Test
    public void testGettingWikiPagesIndexByProject() throws Exception {        
        Project project = new 
        Project();        
        project.setIdentifier("foo");
        
        HttpResponse response = 
                new BasicHttpResponse(HttpVersion.HTTP_1_1,HttpStatus.SC_OK,"OK");       
        String JSON = MyIOUtils.getResourceAsString("redmine_wikipages.json");
        
        response.setEntity(
                new StringEntity(
                        JSON, 
                        ContentType.APPLICATION_JSON));
        
        when(client.execute(any(HttpUriRequest.class))).thenReturn(response);
        
        List<WikiPage> wikiPages = manager.getWikiPagesByProject(project);
        
        WikiPage firstPage = wikiPages.get(0);
        
        assertThat("All pages are returned",wikiPages.size(), is(3));
        assertThat("The startPage is the first one", firstPage.getTitle(), is("Wiki"));
    
        verify(client,times(1)).execute(any(HttpUriRequest.class));
    
        ArgumentCaptor<HttpUriRequest> argument = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(client).execute(argument.capture());
        
        assertThat(
            "Checking requested URI", 
            argument.getValue().getURI().getPath(),
            is("/projects/foo/wiki/index.json")
        );
        
    }
    
    @Test
    public void testGettingSpecificWikiPageByProject() throws Exception {
        Project project = new 
        Project();        
        project.setIdentifier("foo");
        
        HttpResponse response = 
                new BasicHttpResponse(HttpVersion.HTTP_1_1,HttpStatus.SC_OK,"OK");       
        String JSON = MyIOUtils.getResourceAsString("redmine_wikipagedetail.json");
        
        response.setEntity(
                new StringEntity(
                        JSON, 
                        ContentType.APPLICATION_JSON));
        
        when(client.execute(any(HttpUriRequest.class))).thenReturn(response);
        
        WikiPageDetail specificPage = 
                manager.getWikiPageDetailByProjectAndTitle(project,"\"Specific");
        
        assertThat(
            "The startPage is the first one", 
            specificPage.getTitle(), 
            is("\"Specific")
        );
        
        assertEquals("blablabla",specificPage.getText());
        assertEquals("Wiki",specificPage.getParent().getTitle());
        assertEquals(Integer.valueOf(1),specificPage.getUser().getId());
        assertEquals(Integer.valueOf(2),specificPage.getVersion());
        assertNotNull(specificPage.getCreatedOn());
        assertNotNull(specificPage.getUpdatedOn());
	assertNotNull(specificPage.getAttachments());
	assertEquals(1, specificPage.getAttachments().size());
	
	Attachment attachment = specificPage.getAttachments().get(0);
	
	assertEquals("Calculations.groovy",attachment.getFileName());
	assertEquals(Integer.valueOf(4858), attachment.getId());
	assertEquals(171, attachment.getFileSize());
	assertEquals("Stephen Hawking", attachment.getAuthor().getFullName());
        assertEquals("http://redmine.local/attachments/download/4858/Calculations.groovy", attachment.getContentURL());
	
        verify(client,times(1)).execute(any(HttpUriRequest.class));
        ArgumentCaptor<HttpUriRequest> argument = ArgumentCaptor.forClass(HttpUriRequest.class);
        verify(client).execute(argument.capture());
        
        assertThat(
            "Checking requested URI", 
            argument.getValue().getURI().getPath(),
            is("/projects/foo/wiki/\"Specific.json")
        );
    }
    
}
