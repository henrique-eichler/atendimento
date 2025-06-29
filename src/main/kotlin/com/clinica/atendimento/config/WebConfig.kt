package com.clinica.atendimento.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.resource.PathResourceResolver
import java.io.IOException

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        // This resource handler ensures that all requests that don't match a static resource
        // are forwarded to index.html for client-side routing
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(object : PathResourceResolver() {
                    @Throws(IOException::class)
                    override fun getResource(resourcePath: String, location: Resource): Resource? {
                        val requestedResource = location.createRelative(resourcePath)

                        // If the requested resource exists, return it
                        if (requestedResource.exists() && requestedResource.isReadable()) {
                            return requestedResource
                        }

                        // Otherwise, return index.html for client-side routing
                        return ClassPathResource("/static/index.html")
                    }
                })
    }
}