from django.conf.urls import url
from rest_framework.urlpatterns import format_suffix_patterns

from ic_shop.api import article, tag, chapter

urlpatterns = [
    url(r'^tag/$', tag.TagList.as_view()),
    url(r'^tag/(?P<pk>[0-9]+)/$', tag.TagDetail.as_view()),
    url(r'^article/$', article.ArticleList.as_view()),
    url(r'^article/(?P<pk>[0-9]+)/$', article.ArticleDetail.as_view()),
    url(r'^chapter/$', chapter.ChapterList.as_view()),
    url(r'^chapter/(?P<pk>[0-9]+)/$', chapter.ChapterDetail.as_view()),
]

urlpatterns = format_suffix_patterns(urlpatterns)
