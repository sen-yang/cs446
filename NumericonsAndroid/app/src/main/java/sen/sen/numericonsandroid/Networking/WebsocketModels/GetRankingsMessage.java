package sen.sen.numericonsandroid.Networking.WebsocketModels;

import java.util.List;

import sen.sen.numericonsandroid.Global.Constants;
import sen.sen.numericonsandroid.Models.User;

public class GetRankingsMessage extends WebsocketMessage{
  private int limit;
  private int offset;

  public GetRankingsMessage(int limit, int offset){
    super(Constants.MESSAGE_TYPE.GET_RANKINGS);
    this.limit = limit;
    this.offset = offset;
  }
}
