package brickteamdevelopment.brickpong;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

public class GamesApiHandler implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private final Context                                       context;
    private GoogleApiClient                                     mGoogleApiClient;
    private final GoogleApiClient.OnConnectionFailedListener    connectionFailedListener;

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

    public void checkForAchievements( int points ){

        if( mGoogleApiClient.isConnected() ) {
            /*switch ( points ){
                case 0 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_st_happens ) ) ;
                    break ;
                case 1 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_its_a_long_way_to_the_top ) ) ;
                    break ;
                case 50 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_come_on_only_fifty ) ) ;
                    break ;
                case 100 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_0_01 ) ) ;
                    break ;
                case 500 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_youre_doing_it_right ) ) ;
                    break ;
                case 1000 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_gringo ) ) ;
                    break ;
                case 5000 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_good_boy ) ) ;
                    break ;
                case 10000 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_be_stronger_and_stronger ) ) ;
                    break ;
                case 25000 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_iron_man ) ) ;
                    break ;
                case 50000 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_button_destroyer ) ) ;
                    break ;
                case 100000 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_god_of_war ) ) ;
                    break;
                case 250000 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_totally_insane) ) ;
                    break ;
                case 500000 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_half_way_to_olympus ) ) ;
                    break ;
                case 750000 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_from_rags_to_riches ) ) ;
                    break ;
                case 1000000 :
                    Games.Achievements.unlock( mGoogleApiClient , context.getResources().getString( R.string.achievement_most_bored_player ) ) ;
                    break ;

            }*/
        }
    }

    @Override
    public void onConnected(Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

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
