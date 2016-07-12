package brickteamdevelopment.brickpong;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

public class GamesApiHandler implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private final Context context ;
    private GoogleApiClient mGoogleApiClient ;

    private final GoogleApiClient.OnConnectionFailedListener connectionFailedListener ;

    public GamesApiHandler(Context applicationContext , GoogleApiClient.OnConnectionFailedListener listener ){

        context = applicationContext ;
        connectionFailedListener = listener ;

        mGoogleApiClient = new GoogleApiClient.Builder( context )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi( Games.API ).addScope( Games.SCOPE_GAMES )
                .build() ;

        mGoogleApiClient.connect() ;
    }

    public void connect(){
        if( mGoogleApiClient != null )
            mGoogleApiClient.connect() ;
    }

    public void disconnect(){
        if( mGoogleApiClient != null )
            mGoogleApiClient.disconnect(); ;
    }

    public Boolean checkConnection(){
        if( mGoogleApiClient.isConnected() )
            return true ;
        else
            return false ;
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        connectionFailedListener.onConnectionFailed( connectionResult ) ;
    }


    public void updateRecord(int points){
        if ( mGoogleApiClient.isConnected() ) {
            Games.Leaderboards.submitScore( mGoogleApiClient , context.getResources().getString( R.string.leaderboard_score ), points ) ;
        }
    }

    public GoogleApiClient getApiClient(){
        return mGoogleApiClient ;
    }
}
