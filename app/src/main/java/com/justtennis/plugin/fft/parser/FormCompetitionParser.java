package com.justtennis.plugin.fft.parser;

import com.justtennis.plugin.fft.query.request.FFTFindCompetitionFormRequest;
import com.justtennis.plugin.fft.query.response.FindCompetitionFormResponse;
import org.cameleon.android.shared.parser.AbstractFormParser;
import org.cameleon.android.shared.query.request.AbstractFormRequest;
import org.cameleon.android.shared.query.response.AbstractFormResponse;

import org.jsoup.nodes.Element;

public class FormCompetitionParser extends AbstractFormParser {

    private FormCompetitionParser() {}

    private static FormCompetitionParser instance;

    public static FormCompetitionParser getInstance() {
        if (instance == null) {
            instance = new FormCompetitionParser();
        }
        return instance;
    }

    @Override
    protected void parseFormExtra(AbstractFormRequest request, AbstractFormResponse response, Element form) {
        FindCompetitionFormResponse ret = (FindCompetitionFormResponse)response;
        FFTFindCompetitionFormRequest req = (FFTFindCompetitionFormRequest)request;
        ret.type = parseElement(form, req.typeQuery);
        ret.city = parseElement(form, req.cityQuery);
        ret.dateStart = parseElement(form, req.dateStartQuery);
        ret.dateEnd = parseElement(form, req.dateEndQuery);
    }

    public FindCompetitionFormResponse parseForm(String content, FFTFindCompetitionFormRequest request) {
        return (FindCompetitionFormResponse) parseForm(content, request, new FindCompetitionFormResponse());
    }
}
