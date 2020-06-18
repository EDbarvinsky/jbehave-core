package org.jbehave.core.model;

import org.apache.commons.lang3.StringUtils;
import org.jbehave.core.model.ExamplesTable.TableProperties;
import org.junit.Test;

import java.util.Properties;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Valery Yatsynovich
 */
public class TablePropertiesBehaviour {

    @Test
    public void canGetCustomProperties() {
        TableProperties properties = new TableProperties("ignorableSeparator=!--,headerSeparator=!,valueSeparator=!,"
                + "commentSeparator=#,trim=false,metaByRow=true,transformer=CUSTOM_TRANSFORMER", "|", "|",
                "|--");
        assertThat(properties.getRowSeparator(), equalTo("\n"));
        assertThat(properties.getHeaderSeparator(), equalTo("!"));
        assertThat(properties.getValueSeparator(), equalTo("!"));
        assertThat(properties.getIgnorableSeparator(), equalTo("!--"));
        assertThat(properties.getCommentSeparator(), equalTo("#"));
        assertThat(properties.isTrim(), is(false));
        assertThat(properties.isMetaByRow(), is(true));
        assertThat(properties.getTransformer(), equalTo("CUSTOM_TRANSFORMER"));
    }

    @Test
    public void canSetPropertiesWithBackwardSlash() {
        TableProperties properties = new TableProperties("custom=\\", "|", "|", "|--");
        assertThat(properties.getProperties().getProperty("custom"), equalTo("\\"));
    }

    @Test
    public void canGetDefaultProperties() {
        TableProperties properties = new TableProperties(new Properties());
        assertThat(properties.getHeaderSeparator(), equalTo("|"));
        assertThat(properties.getValueSeparator(), equalTo("|"));
        assertThat(properties.getIgnorableSeparator(), equalTo("|--"));
        assertThat(properties.getCommentSeparator(), equalTo("#"));
        assertThat(properties.isTrim(), is(true));
        assertThat(properties.isMetaByRow(), is(false));
        assertThat(properties.getTransformer(), is(nullValue()));
    }

    @Test
    public void canGetAllProperties() {
        Properties properties = new Properties();
        properties.setProperty("key", "value");
        TableProperties tableProperties = new TableProperties(properties);
        assertThat(tableProperties.getProperties().containsKey("key"), is(true));
    }

    @Test
    public void canGetPropertiesWithNestedTransformersWithoutEscaping() {
        TableProperties properties = new TableProperties("transformer=CUSTOM_TRANSFORMER, " +
                "tables={transformer=CUSTOM_TRANSFORMER\\, parameter1=value1}", "|", "|",
                "|--");
        assertThat(properties.getRowSeparator(), equalTo("\n"));
        assertThat(properties.getHeaderSeparator(), equalTo("|"));
        assertThat(properties.getValueSeparator(), equalTo("|"));
        assertThat(properties.getIgnorableSeparator(), equalTo("|--"));
        assertThat(properties.isTrim(), is(true));
        assertThat(properties.isMetaByRow(), is(false));
        assertThat(properties.getTransformer(), equalTo("CUSTOM_TRANSFORMER"));
        assertThat(properties.getProperties().getProperty("tables"),
                equalTo("{transformer=CUSTOM_TRANSFORMER, parameter1=value1}"));
    }

    @Test
    public void shouldNotTrimPropertyValue() {
        String propertiesAsString = "value= , trimProperty_value=false";
        TableProperties tableProperties = new TableProperties(propertiesAsString, "|", "|",
                "|--");
        assertThat(tableProperties.getProperties().getProperty("value"), equalTo(" "));
    }

    @Test
    public void shouldTrimPropertyValue() {
        String propertiesAsString = "value= , trimProperty_value=true";
        TableProperties tableProperties = new TableProperties(propertiesAsString, "|", "|",
                "|--");
        assertThat(tableProperties.getProperties().getProperty("value"), equalTo(StringUtils.EMPTY));
    }

    @Test
    public void shouldTrimPropertyValueWrongParameter() {
        String propertiesAsString = "value= , trimProperty_anotherValue=false";
        TableProperties tableProperties = new TableProperties(propertiesAsString, "|", "|",
                "|--");
        assertThat(tableProperties.getProperties().getProperty("value"), equalTo(StringUtils.EMPTY));
    }

    @Test
    public void shouldTrimByDefault() {
        String propertiesAsString = "value= ";
        TableProperties tableProperties = new TableProperties(propertiesAsString, "|", "|",
                "|--");
        assertThat(tableProperties.getProperties().getProperty("value"), equalTo(StringUtils.EMPTY));
    }
}
